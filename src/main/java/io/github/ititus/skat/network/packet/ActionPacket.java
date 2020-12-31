package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.game.action.Action;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ActionPacket implements ServerboundPacket {

    private final Action action;

    public ActionPacket(Action action) {
        this.action = action;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        buf.writeEnum(action.getType());
        action.write(buf);
    }

    @Override
    public ServerboundPacketType getServerboundType() {
        return ServerboundPacketType.ACTION;
    }
}
