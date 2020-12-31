package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class RoundDoneEvent extends Event {

    private final long[] scoreTotal;

    public RoundDoneEvent(ReadablePacketBuffer buf) {
        super(Type.ROUND_DONE, buf);
        scoreTotal = buf.readLongs(4);
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
