package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class CallGameAction extends Action {

    public CallGameAction(ReadablePacketBuffer buf) {
        super(Type.CALL_GAME);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }
}
