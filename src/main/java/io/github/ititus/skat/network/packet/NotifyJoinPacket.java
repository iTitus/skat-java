package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.netty.channel.ChannelHandlerContext;

public class NotifyJoinPacket implements ClientboundPacket {

    private final Player player;

    public NotifyJoinPacket(ReadablePacketBuffer buf) {
        player = new Player(buf);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.NOTIFY_JOIN;
    }
}
