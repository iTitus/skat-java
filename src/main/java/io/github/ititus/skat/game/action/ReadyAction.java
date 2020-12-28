package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ReadyAction extends Action {

    public ReadyAction(ReadablePacketBuffer buf) {
        super(Type.READY);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }
}
