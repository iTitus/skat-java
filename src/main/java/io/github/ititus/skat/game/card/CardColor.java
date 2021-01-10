package io.github.ititus.skat.game.card;

import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.util.Precondition;

public enum CardColor implements NetworkEnum<CardColor> {

    KARO,
    HERZ,
    PIK,
    KREUZ;

    public static CardColor fromId(byte id) {
        CardColor[] values = values();

        Precondition.checkBounds(id, 0, values.length + 1);

        int ordinal = id - 1;
        if (ordinal == -1) {
            return null;
        }

        return values[ordinal];
    }

    @Override
    public byte getId() {
        int id = ordinal() + 1;

        Precondition.checkBounds(id, 0, Byte.MAX_VALUE);

        return (byte) id;
    }
}
