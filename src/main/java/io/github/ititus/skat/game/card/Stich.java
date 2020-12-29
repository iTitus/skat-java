package io.github.ititus.skat.game.card;

import io.github.ititus.skat.game.Player;

public final class Stich {

    private final Card[] cards;
    private final Player vorhand;
    private final Player winner;


    public Stich(Card[] cards, Player vorhand, Player winner) {
        this.cards = cards;
        this.vorhand = vorhand;
        this.winner = winner;
    }

    public int getPlayedCards() {
        return cards.length;
    }
}
