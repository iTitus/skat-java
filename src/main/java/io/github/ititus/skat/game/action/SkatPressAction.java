package io.github.ititus.skat.game.action;

import io.github.ititus.skat.game.card.Card;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class SkatPressAction extends Action {

    private final Card card1;
    private final Card card2;

    public SkatPressAction(long id, Card card1, Card card2) {
        super(Type.SKAT_PRESS, id);
        this.card1 = card1;
        this.card2 = card2;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        super.write(buf);
        buf.writeEnum(card1);
        buf.writeEnum(card2);
    }
}
