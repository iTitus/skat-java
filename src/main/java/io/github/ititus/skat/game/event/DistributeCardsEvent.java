package io.github.ititus.skat.game.event;

import io.github.ititus.skat.SkatClient;
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
    public Optional<GameState> visit(SkatClient c, GameState g) {
        return g.apply(c, this);
    }
}
