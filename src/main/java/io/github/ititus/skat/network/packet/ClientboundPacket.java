package io.github.ititus.skat.network.packet;

import io.netty.channel.ChannelHandlerContext;

public interface ClientboundPacket {

    void handle(ChannelHandlerContext ctx);

    ClientboundPacketType getClientboundType();

}
