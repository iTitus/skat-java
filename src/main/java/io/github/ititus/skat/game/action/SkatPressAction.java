package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class SkatPressAction extends Action {

    public SkatPressAction() {
        super(Type.SKAT_PRESS);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }
}
