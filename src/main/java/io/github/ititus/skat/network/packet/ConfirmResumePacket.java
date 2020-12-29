package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.gui.JoiningGui;
import io.github.ititus.skat.network.ConnectionState;
import io.github.ititus.skat.network.NetworkManager;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;

public class ConfirmResumePacket implements ClientboundPacket {

    private final byte gupid;

    public ConfirmResumePacket(ReadablePacketBuffer buf) {
        gupid = buf.readByte();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, SkatClient skatClient) {
        if (ctx.channel().attr(NetworkManager.CONNECTION_STATE_KEY).get() != ConnectionState.JOIN) {
            throw new IllegalStateException("expected connection state join");
        }

        Platform.runLater(() -> skatClient.getCurrentGui()
                .filter(gui -> gui instanceof JoiningGui)
                .map(gui -> (JoiningGui) gui)
                .ifPresentOrElse(gui -> gui.confirmResume(gupid), () -> {
                    throw new IllegalStateException("Expected JoiningGui");
                }));
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.CONFIRM_RESUME;
    }
}
