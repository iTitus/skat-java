package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.game.action.Action;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ActionPacket extends Packet {

    private final long id;
    private final Action action;

    public ActionPacket(ReadablePacketBuffer buf) {
        super(PacketType.ACTION);
        Action.Type type = buf.readEnum(Action.Type::fromId);
        id = buf.readLong();
        action = type.read(buf);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public void handleClient() {
        throw new UnsupportedOperationException("not handled on client side");
    }
}
