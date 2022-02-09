package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.gui.JoiningGui;
import io.github.ititus.skat.network.ConnectionState;
import io.github.ititus.skat.network.NetworkManager;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;

import static io.github.ititus.commons.precondition.Precondition.equalTo;
import static io.github.ititus.commons.precondition.Preconditions.check;

public class ConfirmJoinPacket implements ClientboundPacket {

    private final byte gupid;

    public ConfirmJoinPacket(ReadablePacketBuffer buf) {
        gupid = buf.readByte();
    }

    public byte getGupid() {
        return gupid;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, SkatClient skatClient) {
        check(ctx.channel().attr(NetworkManager.CONNECTION_STATE_KEY).get(), equalTo(ConnectionState.JOIN));

        Platform.runLater(() -> skatClient.getCurrentGui(JoiningGui.class)
                .ifPresentOrElse(gui -> gui.confirmJoin(gupid), () -> {
                    throw new IllegalStateException("Expected JoiningGui");
                }));
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.CONFIRM_JOIN;
    }
}
