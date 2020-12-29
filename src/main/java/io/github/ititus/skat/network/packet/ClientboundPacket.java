package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.SkatClient;
import io.netty.channel.ChannelHandlerContext;

public interface ClientboundPacket {

    void handle(ChannelHandlerContext ctx, SkatClient skatClient);

    ClientboundPacketType getClientboundType();

}
