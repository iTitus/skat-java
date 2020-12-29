package io.github.ititus.skat.game.card;

public enum CardType {

    TYPE_7,
    TYPE_8,
    TYPE_9,
    TYPE_D,
    TYPE_K,
    TYPE_10,
    TYPE_A,
    TYPE_B;

    public byte getId() {
        return (byte) (ordinal() + 1);
    }

}
