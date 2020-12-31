package io.github.ititus.skat.game.card;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Arrays;

public final class NetworkStich {

    /**
     * indexed by vorhand + active player
     */
    private final Card[] cards;
    private final short playedCards;
    /**
     * indexed by active player
     */
    private final byte vorhand;
    /**
     * indexed by active player
     */
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

    public Stich get(SkatClient skatClient) {
        return new Stich(
                Arrays.copyOf(cards, playedCards),
                skatClient.getPlayer(vorhand),
                skatClient.getPlayer(winner)
        );
    }
}
