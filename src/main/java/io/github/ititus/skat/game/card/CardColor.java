package io.github.ititus.skat.game.card;

import io.github.ititus.skat.network.NetworkEnum;

import static io.github.ititus.precondition.IntPrecondition.inBounds;
import static io.github.ititus.precondition.IntPrecondition.inBoundsInclusive;
import static io.github.ititus.precondition.Preconditions.check;

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
        }

        check(id, inBounds(values.length));
        return values[ordinal];
    }

    @Override
    public byte getId() {
        int id = ordinal() + 1;

        check(id, inBoundsInclusive(Byte.MAX_VALUE));
        return (byte) id;
    }
}
