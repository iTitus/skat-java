package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class AnnounceScoresEvent extends Event {

    public AnnounceScoresEvent(ReadablePacketBuffer buf) {
        super(Type.ANNOUNCE_SCORES);
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
