package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class SkatLeaveAction extends Action {

    public SkatLeaveAction(ReadablePacketBuffer buf) {
        super(Type.SKAT_LEAVE);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }
}
