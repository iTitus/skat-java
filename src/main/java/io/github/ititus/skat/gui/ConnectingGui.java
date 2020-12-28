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
                        () -> Platform.runLater(() -> main.disconnect("Lost connection to server"))
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

        main.disconnect("Connection failure: " + cause);
    }

    @Override
    public void close() {
        if (cancel) {
            return;
        }

        cancel = true;
        main.disconnect("Cancelling connection");
    }
}
