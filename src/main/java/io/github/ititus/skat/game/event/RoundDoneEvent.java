package io.github.ititus.skat.game.event;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.game.gamestate.ScoreBoard;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class RoundDoneEvent extends Event {

    private final ScoreBoard scoreTotal;

    public RoundDoneEvent(ReadablePacketBuffer buf) {
        super(Type.ROUND_DONE, buf);
        scoreTotal = ScoreBoard.read(buf);
    }

    @Override
    public Optional<GameState> visit(SkatClient c, GameState g) {
        return g.apply(c, this);
    }
}
