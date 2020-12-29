package io.github.ititus.skat.game.card;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

public final class NetworkStich {

    private final Card[] cards;
    private final short playedCards;
    private final byte vorhand;
    private final byte winner;

    public NetworkStich(ReadablePacketBuffer buf) {
        cards = new Card[3];
        for (int ap = 0; ap < 3; ap++) {
            cards[ap] = Card.read(buf);
        }
        playedCards = buf.readUnsignedByte();
        vorhand = buf.readByte();
        winner = buf.readByte();
    }
}
