package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.card.Card;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class PlayCardEvent extends Event {

    private final Card card;

    public PlayCardEvent(ReadablePacketBuffer buf) {
        super(Type.PLAY_CARD, buf);
        card = buf.readEnum(Card::fromId);
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
