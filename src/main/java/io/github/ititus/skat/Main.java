package io.github.ititus.skat;

import io.github.ititus.skat.gui.ConnectGui;
import io.github.ititus.skat.gui.ExitingGui;
import io.github.ititus.skat.gui.Gui;
import io.github.ititus.skat.network.NetworkManager;
import io.netty.channel.ChannelFuture;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application {

    private final AtomicBoolean exit = new AtomicBoolean(false);
    private Stage stage;
    private NetworkManager networkManager;

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

    public void stopNetworkManager() {
        if (networkManager != null) {
            NetworkManager mgr = networkManager;
            networkManager = null;
            mgr.stop();
        }
    }

    public Optional<ChannelFuture> stopNetworkManagerAsync() {
        if (networkManager != null) {
            NetworkManager mgr = networkManager;
            networkManager = null;
            return mgr.stopAsync();
        }

        return Optional.empty();
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

    public Stage getStage() {
        return stage;
    }

    public boolean isExit() {
        return exit.get();
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
}
