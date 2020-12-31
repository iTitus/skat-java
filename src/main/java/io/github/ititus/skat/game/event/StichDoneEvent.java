package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class StichDoneEvent extends Event {

    private final byte stichWinner;

    public StichDoneEvent(ReadablePacketBuffer buf) {
        super(Type.SKAT_PRESS, buf);
        stichWinner = buf.readByte();
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
