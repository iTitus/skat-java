package io.github.ititus.skat.gui;

import io.github.ititus.skat.network.NetworkManager;
import javafx.application.Platform;

public class ConnectingGui extends LoadingGui {

    private final String host;
    private final int port;

    private boolean cancel;

    public ConnectingGui(String host, int port) {
        super("Connecting...");
        this.host = host;
        this.port = port;
    }

    @Override
    public void onOpen() {
        main.setNetworkManager(
                new NetworkManager(host, port,
                        () -> Platform.runLater(this::onSuccess),
                        cause -> Platform.runLater(() -> onFailure(cause)),
                        () -> {
                            if (!main.isExit()) {
                                main.stopNetworkManagerAsync();
                                Platform.runLater(() -> {
                                    ConnectGui gui = (ConnectGui) previousGui;
                                    gui.showError("Lost connection to server");
                                    main.openGui(gui, true);
                                });
                            }
                        }
                )
        );
    }

    public void onSuccess() {
        if (cancel) {
            return;
        }

        main.openGui(new JoinGui());
    }

    public void onFailure(Throwable cause) {
        if (cancel) {
            return;
        }

        getPreviousGui().showError("Connection failure: " + cause);
        close();
    }

    private ConnectGui getPreviousGui() {
        return (ConnectGui) previousGui;
    }

    @Override
    public void close() {
        if (cancel) {
            return;
        }

        cancel = true;
        setLoadingText("Closing connection...");

        main.stopNetworkManagerAsync().ifPresentOrElse(
                f -> f.addListener(f_ -> Platform.runLater(super::close)),
                super::close
        );
    }
}
