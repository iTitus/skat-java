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
        skatClient.setNetworkManager(
                new NetworkManager(skatClient, host, port,
                        () -> Platform.runLater(this::onSuccess),
                        cause -> Platform.runLater(() -> onFailure(cause)),
                        () -> Platform.runLater(() -> skatClient.disconnect("Lost connection to server"))
                )
        );
    }

    public void onSuccess() {
        if (cancel) {
            return;
        }

        skatClient.openGui(new JoinGui());
    }

    public void onFailure(Throwable cause) {
        if (cancel) {
            return;
        }

        skatClient.disconnect("Connection failure: " + cause);
    }

    @Override
    public void close() {
        if (cancel) {
            return;
        }

        cancel = true;
        skatClient.disconnect("Cancelling connection");
    }

    @Override
    protected boolean closeOnEsc() {
        return true;
    }
}
