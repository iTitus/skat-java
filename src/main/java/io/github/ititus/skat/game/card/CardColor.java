package io.github.ititus.skat.game.card;

import io.github.ititus.skat.network.NetworkEnum;

public enum CardColor implements NetworkEnum<CardColor> {

    KARO,
    HERZ,
    PIK,
    KREUZ;

    public static CardColor fromId(byte id) {
        CardColor[] values = values();
        int ordinal = id - 1;
        if (ordinal == -1) {
            return null;
        } else if (ordinal < 0 || ordinal >= values.length) {
            throw new IndexOutOfBoundsException("id out of bounds");
        }

        return values[ordinal];
    }

    @Override
    public byte getId() {
        int id = ordinal() + 1;
        if (id < 0 || id > Byte.MAX_VALUE) {
            throw new IndexOutOfBoundsException("ordinal out of bounds");
        }

        return (byte) id;
    }
}
