package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.game.action.Action;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ActionPacket implements ServerboundPacket {

    private final long actionId;
    private final Action action;

    public ActionPacket(long actionId, Action action) {
        this.actionId = actionId;
        this.action = action;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        buf.writeEnum(action.getType());
        buf.writeLong(actionId);
        action.write(buf);
    }

    @Override
    public ServerboundPacketType getServerboundType() {
        return ServerboundPacketType.ACTION;
    }
}
