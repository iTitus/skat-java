package io.github.ititus.skat.game.card;

import io.github.ititus.skat.network.buffer.PacketBufferSerializer;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

import java.util.EnumSet;

public final class CardCollection implements PacketBufferSerializer {

    private final EnumSet<Card> cards;

    private CardCollection(EnumSet<Card> cards) {
        this.cards = cards;
    }

    public static CardCollection empty() {
        return new CardCollection(EnumSet.noneOf(Card.class));
    }

    public static CardCollection all() {
        return new CardCollection(EnumSet.allOf(Card.class));
    }

    public static CardCollection read(ReadablePacketBuffer buf) {
        EnumSet<Card> cards = EnumSet.noneOf(Card.class);

        long content = buf.readUnsignedInt();
        for (byte i = 0; i < Card.MAX_INDEX; i++) {
            if ((content & (1L << i)) != 0) {
                cards.add(Card.fromIndex(i));
            }
        }

        return new CardCollection(cards);
    }

    public CardCollection add(Card c) {
        EnumSet<Card> newCards = EnumSet.copyOf(cards);
        newCards.add(c);
        return new CardCollection(newCards);
    }

    public CardCollection remove(Card c) {
        EnumSet<Card> newCards = EnumSet.copyOf(cards);
        newCards.remove(c);
        return new CardCollection(newCards);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        long content = 0;
        for (Card c : cards) {
            content |= 1L << c.getIndex();
        }

        buf.writeUnsignedInt(content);
    }
}
