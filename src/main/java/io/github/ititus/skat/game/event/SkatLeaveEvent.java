package io.github.ititus.skat.game.event;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class SkatLeaveEvent extends Event {

    public SkatLeaveEvent(ReadablePacketBuffer buf) {
        super(Type.SKAT_LEAVE, buf);
    }

    @Override
    public Optional<GameState> visit(SkatClient c, GameState g) {
        return g.apply(c, this);
    }
}
