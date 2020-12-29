package io.github.ititus.skat.game.card;

public enum CardColor {

    KARO,
    HERZ,
    PIK,
    KREUZ;

    public byte getId() {
        return (byte) (ordinal() + 1);
    }
}
