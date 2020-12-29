package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class SkatTakeAction extends Action {

    public SkatTakeAction() {
        super(Type.SKAT_TAKE);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }
}
