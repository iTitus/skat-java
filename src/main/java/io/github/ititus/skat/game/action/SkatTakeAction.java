package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class SkatTakeAction extends Action {

    public SkatTakeAction(ReadablePacketBuffer buf) {
        super(Type.SKAT_TAKE);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }
}
