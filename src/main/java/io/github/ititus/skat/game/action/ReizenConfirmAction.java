package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ReizenConfirmAction extends Action {

    public ReizenConfirmAction(ReadablePacketBuffer buf) {
        super(Type.REIZEN_CONFIRM);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }
}
