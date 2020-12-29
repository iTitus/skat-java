package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.game.gamestate.NetworkGameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.netty.channel.ChannelHandlerContext;

public class ResyncPacket implements ClientboundPacket {

    private final NetworkGameState gameState;

    public ResyncPacket(ReadablePacketBuffer buf) {
        gameState = new NetworkGameState(buf);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.RESYNC;
    }
}
