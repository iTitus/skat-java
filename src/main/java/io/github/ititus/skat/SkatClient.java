package io.github.ititus.skat;

import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.game.gamestate.NetworkGameState;
import io.github.ititus.skat.gui.*;
import io.github.ititus.skat.network.ConnectionState;
import io.github.ititus.skat.network.NetworkManager;
import io.github.ititus.skat.network.packet.ConfirmResyncPacket;
import io.netty.channel.ChannelFuture;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class SkatClient extends Application {

    public static final byte MAX_PLAYERS = 4;
    public static final byte ACTIVE_PLAYERS = 3;

    private final AtomicBoolean exit = new AtomicBoolean(false);
    private final AtomicBoolean disconnect = new AtomicBoolean(false);
    private final Byte2ObjectMap<Player> players = new Byte2ObjectOpenHashMap<>();
    private Stage stage;
    private NetworkManager networkManager;
    private GameState gameState;
    private Player player;

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

        gui.setSkatClient(this);

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
        if (disconnect.get() || exit.get() || getCurrentGui(ConnectGui.class).isPresent()) {
            return;
        }

        disconnect.set(true);
        openGui(new LoadingGui("Closing connection..."));

        Runnable r = () -> {
            Optional<Gui> rootGuiOpt = getCurrentGui();

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

    public <T extends Gui> Optional<T> getCurrentGui(Class<T> clazz) {
        return Optional.ofNullable(stage)
                .map(Stage::getScene)
                .map(Scene::getRoot)
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    public Optional<Gui> getCurrentGui() {
        return getCurrentGui(Gui.class);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setup(byte gupid, String name) {
        player = new Player(gupid, name);

        players.clear();
        players.put(gupid, player);
    }

    public void startResync() {
        networkManager.setConnectionState(ConnectionState.RESYNC);
        openGui(new ResyncGui());
    }

    public void resync(NetworkGameState ngs, Byte2ObjectMap<Player> players) {
        this.players.clear();
        this.players.putAll(players);

        this.player = this.players.get(this.player.getGupid());

        this.gameState = ngs.get(this);

        networkManager.sendPacket(new ConfirmResyncPacket(),
                f -> {
                    networkManager.setConnectionState(ConnectionState.INGAME);
                    openGui(new IngameGui());
                });
    }

    public void setActivePlayerIndices(byte[] activePlayerIndices) {
        if (activePlayerIndices.length != ACTIVE_PLAYERS) {
            throw new IllegalArgumentException("activePlayerIndices must be of length " + ACTIVE_PLAYERS + " but was "
                    + activePlayerIndices.length);
        } else if (players.size() < ACTIVE_PLAYERS) {
            throw new IllegalStateException("players must be at least be of size " + ACTIVE_PLAYERS + " but was " + players.size());
        }

        for (byte ap = 0; ap < ACTIVE_PLAYERS; ap++) {
            byte gupid = activePlayerIndices[ap];
            Player current = getPlayer(gupid);
            if (current == null) {
                throw new IllegalArgumentException("all gupids must be valid but got invalid one for ap " + ap);
            }

            if (current.getActivePlayerIndex() != ap) {
                players.put(gupid, current.withActivePlayerIndex(ap));
            }
        }
    }

    public Player getPlayer(byte gupid) {
        if (gupid == -1) {
            return null;
        }

        Player player = players.get(gupid);
        if (player == null) {
            throw new NoSuchElementException("unknown player gupid");
        }

        return player;
    }

    public Player getActivePlayer(byte ap) {
        if (ap == -1) {
            return null;
        }

        if (gameState == null) {
            throw new IllegalStateException("cannot get active player while there is no game state");
        }

        Player activePlayer = gameState.getActivePlayers()[ap];
        if (activePlayer == null) {
            throw new IllegalStateException("cannot get active player while not in a round");
        }

        return activePlayer;
    }
}
