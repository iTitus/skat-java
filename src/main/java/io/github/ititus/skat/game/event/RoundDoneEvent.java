package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

import java.util.Optional;

public class RoundDoneEvent extends Event {

    public RoundDoneEvent(ReadablePacketBuffer buf) {
        super(Type.ROUND_DONE);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
