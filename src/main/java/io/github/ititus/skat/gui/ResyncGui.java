package io.github.ititus.skat.gui;

import io.github.ititus.skat.network.packet.RequestResyncPacket;

public class ResyncGui extends LoadingGui {

    public ResyncGui() {
        super("Resyncing...");
    }

    @Override
    public void onOpen() {
        skatClient.getNetworkManager().sendPacket(new RequestResyncPacket());
    }
}
