package io.github.ititus.skat;

import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.gui.ConnectGui;
import io.github.ititus.skat.gui.ExitingGui;
import io.github.ititus.skat.gui.Gui;
import io.github.ititus.skat.gui.LoadingGui;
import io.github.ititus.skat.network.NetworkManager;
import io.netty.channel.ChannelFuture;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class SkatClient extends Application {

    private final AtomicBoolean exit = new AtomicBoolean(false);
    private final AtomicBoolean disconnect = new AtomicBoolean(false);

    private Stage stage;

    private NetworkManager networkManager;
    private Byte2ObjectMap<Player> players;
    private GameState gameState;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        openGui(new ConnectGui(), true);

        primaryStage.setTitle("Skat");
        primaryStage.show();
    }

    @Override
    public void stop() {
        exit.set(true);
        stopNetworkManager();
    }

    public void openGui(Gui gui) {
        openGui(gui, false);
    }

    public void openGui(Gui gui, boolean replace) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> openGui(gui, replace));
            return;
        }

        gui.setMain(this);

        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(gui);
            scene.setOnKeyPressed(event -> Optional.ofNullable(stage)
                    .map(Stage::getScene)
                    .map(Scene::getRoot)
                    .filter(root -> root instanceof Gui)
                    .map(root -> (Gui) root)
                    .map(gui_ -> gui_.onKeyPressed(event))
                    .filter(Boolean::booleanValue)
                    .ifPresent(b -> event.consume())
            );
            stage.setScene(scene);
        } else {
            Parent oldRoot = scene.getRoot();
            if (oldRoot instanceof Gui) {
                Gui oldGui = (Gui) oldRoot;
                if (!replace) {
                    gui.setPreviousGui(oldGui.getGuiForPrevious());
                }
            }

            scene.setRoot(gui);
        }

        stage.sizeToScene();
        stage.setResizable(gui.isResizable());
        gui.onOpen();
    }

    public void exit() {
        exit.set(true);
        openGui(new ExitingGui());
        Platform.exit();
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public void setNetworkManager(NetworkManager networkManager) {
        if (networkManager == null) {
            throw new RuntimeException("cannot set the NetworkManager to null, it needs to be closed first");
        } else if (this.networkManager != null) {
            throw new RuntimeException("cannot change an existing NetworkManager");
        }

        this.networkManager = networkManager;
    }

    public void disconnect(String reason) {
        if (disconnect.get() || exit.get()
                || Optional.ofNullable(stage).map(Stage::getScene).map(Scene::getRoot).filter(root -> root instanceof ConnectGui).isPresent()) {
            return;
        }

        disconnect.set(true);
        openGui(new LoadingGui("Closing connection..."));

        Runnable r = () -> {
            Optional<Gui> rootGuiOpt = Optional.ofNullable(stage)
                    .map(Stage::getScene)
                    .map(Scene::getRoot)
                    .filter(root -> root instanceof Gui)
                    .map(root -> (Gui) root);

            ConnectGui firstGui = null;
            if (rootGuiOpt.isPresent()) {
                Gui rootGui = rootGuiOpt.get();
                while (rootGui != null && !(rootGui instanceof ConnectGui)) {
                    rootGui = rootGui.getPreviousGui();
                }

                if (rootGui != null) {
                    firstGui = (ConnectGui) rootGui;
                }
            }
            if (firstGui == null) {
                firstGui = new ConnectGui();
            }

            if (reason != null) {
                firstGui.showError(reason);
            }

            openGui(firstGui, true);
            disconnect.set(false);
        };

        stopNetworkManagerAsync().ifPresentOrElse(f -> Platform.runLater(r), r);
    }

    private void stopNetworkManager() {
        if (networkManager != null) {
            NetworkManager mgr = networkManager;
            networkManager = null;
            mgr.stop();
        }
    }

    private Optional<ChannelFuture> stopNetworkManagerAsync() {
        if (networkManager != null) {
            NetworkManager mgr = networkManager;
            networkManager = null;
            return mgr.stopAsync();
        }

        return Optional.empty();
    }
}
