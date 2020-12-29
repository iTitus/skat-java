package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ReizenNumberAction extends Action {

    public ReizenNumberAction() {
        super(Type.REIZEN_NUMBER);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }
}
