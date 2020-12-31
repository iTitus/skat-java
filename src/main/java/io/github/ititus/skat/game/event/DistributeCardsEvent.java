package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class DistributeCardsEvent extends Event {

    private final CardCollection hand;

    public DistributeCardsEvent(ReadablePacketBuffer buf) {
        super(Type.DISTRIBUTE_CARDS, buf);
        hand = CardCollection.read(buf);
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
