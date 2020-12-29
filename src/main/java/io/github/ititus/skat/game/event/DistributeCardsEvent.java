package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class DistributeCardsEvent extends Event {

    public DistributeCardsEvent(ReadablePacketBuffer buf) {
        super(Type.DISTRIBUTE_CARDS);
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
