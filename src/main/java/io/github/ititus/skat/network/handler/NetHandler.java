package io.github.ititus.skat.network.handler;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.network.NetworkManager;

public abstract class NetHandler {

    private final SkatClient skatClient;
    private final NetworkManager networkManager;

    protected NetHandler(SkatClient skatClient, NetworkManager networkManager) {
        this.skatClient = skatClient;
        this.networkManager = networkManager;
    }

    public SkatClient getMain() {
        return skatClient;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @FunctionalInterface
    public interface Constructor {

        NetHandler create(SkatClient skatClient, NetworkManager networkManager);

    }
}
