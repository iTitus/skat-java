package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class PlayCardAction extends Action {

    public PlayCardAction(ReadablePacketBuffer buf) {
        super(Type.PLAY_CARD);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }
}
