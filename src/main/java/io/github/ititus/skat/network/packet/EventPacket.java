package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.event.Event;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.netty.channel.ChannelHandlerContext;

public class EventPacket implements ClientboundPacket {

    private final Event event;

    public EventPacket(ReadablePacketBuffer buf) {
        Event.Type type = buf.readEnum(Event.Type::fromId);
        event = type.read(buf);
    }

    @Override
    public void handle(ChannelHandlerContext ctx, SkatClient skatClient) {
        skatClient.handleEvent(event);
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.EVENT;
    }
}
