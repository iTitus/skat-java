package io.github.ititus.skat.network;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.network.handler.JoinNetHandler;
import io.github.ititus.skat.network.handler.NetHandler;

public enum ConnectionState {

    ERROR(null),
    JOIN(JoinNetHandler::new),
    RESYNC(null),
    INGAME(null);

    private final NetHandler.Constructor netHandlerConstructor;

    ConnectionState(NetHandler.Constructor netHandlerConstructor) {
        this.netHandlerConstructor = netHandlerConstructor;
    }

    public NetHandler createNetHandler(SkatClient skatClient, NetworkManager networkManager) {
        return netHandlerConstructor.create(skatClient, networkManager);
    }
}
