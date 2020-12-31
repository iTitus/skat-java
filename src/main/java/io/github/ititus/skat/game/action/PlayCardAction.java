package io.github.ititus.skat.game.action;

import io.github.ititus.skat.game.card.Card;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class PlayCardAction extends Action {

    private final Card card;

    public PlayCardAction(long id, Card card) {
        super(Type.PLAY_CARD, id);
        this.card = card;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        super.write(buf);
        buf.writeEnum(card);
    }
}
