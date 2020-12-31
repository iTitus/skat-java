package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.card.Card;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class SkatTakeEvent extends Event {

    private final Card card1, card2;

    public SkatTakeEvent(ReadablePacketBuffer buf) {
        super(Type.SKAT_TAKE, buf);
        card1 = buf.readNullableEnum(Card::fromId);
        card2 = buf.readNullableEnum(Card::fromId);
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
