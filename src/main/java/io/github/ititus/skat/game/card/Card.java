package io.github.ititus.skat.game.card;

import io.github.ititus.skat.game.gamestate.GameRules;
import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;

import java.util.Comparator;

import static io.github.ititus.skat.game.card.CardColor.*;
import static io.github.ititus.skat.game.card.CardType.*;
import static io.github.ititus.skat.util.precondition.IntPrecondition.inBounds;
import static io.github.ititus.skat.util.precondition.IntPrecondition.inBoundsInclusive;
import static io.github.ititus.skat.util.precondition.Precondition.notNull;
import static io.github.ititus.skat.util.precondition.Preconditions.check;

public enum Card implements NetworkEnum<Card> {

    KARO_7(KARO, TYPE_7, 0),
    KARO_8(KARO, TYPE_8, 1),
    KARO_9(KARO, TYPE_9, 2),
    KARO_D(KARO, TYPE_D, 3),
    KARO_K(KARO, TYPE_K, 4),
    KARO_10(KARO, TYPE_10, 5),
    KARO_A(KARO, TYPE_A, 6),
    KARO_B(KARO, TYPE_B, 28),
    HERZ_7(HERZ, TYPE_7, 7),
    HERZ_8(HERZ, TYPE_8, 8),
    HERZ_9(HERZ, TYPE_9, 9),
    HERZ_D(HERZ, TYPE_D, 10),
    HERZ_K(HERZ, TYPE_K, 11),
    HERZ_10(HERZ, TYPE_10, 12),
    HERZ_A(HERZ, TYPE_A, 13),
    HERZ_B(HERZ, TYPE_B, 29),
    PIK_7(PIK, TYPE_7, 14),
    PIK_8(PIK, TYPE_8, 15),
    PIK_9(PIK, TYPE_9, 16),
    PIK_D(PIK, TYPE_D, 17),
    PIK_K(PIK, TYPE_K, 18),
    PIK_10(PIK, TYPE_10, 19),
    PIK_A(PIK, TYPE_A, 20),
    PIK_B(PIK, TYPE_B, 30),
    KREUZ_7(KREUZ, TYPE_7, 21),
    KREUZ_8(KREUZ, TYPE_8, 22),
    KREUZ_9(KREUZ, TYPE_9, 23),
    KREUZ_D(KREUZ, TYPE_D, 24),
    KREUZ_K(KREUZ, TYPE_K, 25),
    KREUZ_10(KREUZ, TYPE_10, 26),
    KREUZ_A(KREUZ, TYPE_A, 27),
    KREUZ_B(KREUZ, TYPE_B, 31);

    public static final byte MAX_INDEX;
    private static final Byte2ObjectMap<Card> ID_MAP;
    private static final Comparator<Card> ID_COMPARATOR = Comparator.naturalOrder();
    private static final Comparator<Card> PREGAME_HAND_COMPARATOR = Comparator.comparingInt(Card::getPregameHandIndex);

    static {
        Card[] values = values();

        MAX_INDEX = (byte) values.length;
        ID_MAP = new Byte2ObjectOpenHashMap<>(values.length);
        for (Card c : values) {
            ID_MAP.put(c.id, c);
        }
    }

    private final byte id;
    private final CardColor color;
    private final CardType type;
    private final int pregameHandIndex;

    Card(CardColor color, CardType type, int pregameHandIndex) {
        this.pregameHandIndex = pregameHandIndex;
        this.id = (byte) ((type.getId() & 0b1111) | ((color.getId() & 0b111)) << 4);
        this.color = color;
        this.type = type;
    }

    public static Card read(ReadablePacketBuffer buf) {
        short id = buf.readUnsignedByte();
        check(id, inBoundsInclusive(Byte.MAX_VALUE));

        return fromId((byte) id);
    }

    public static Card fromId(byte id) {
        if (id == 0) {
            return null;
        }

        Card c = ID_MAP.get(id);
        check(c, notNull());

        return c;
    }

    public static Card fromIndex(byte index) {
        Card[] values = values();
        check(index, inBounds(values.length));
        return values[index];
    }

    public static Comparator<Card> idComparator() {
        return ID_COMPARATOR;
    }

    public static Comparator<Card> pregameHandComparator() {
        return PREGAME_HAND_COMPARATOR;
    }

    public static Comparator<Card> handComparator(GameRules gr) {
        // TODO: implement
        return null;
    }

    public static Comparator<Card> sticheComparator(GameRules gr) {
        // TODO: implement
        return null;
    }

    @Override
    public byte getId() {
        return id;
    }

    public byte getIndex() {
        return (byte) ordinal();
    }

    public int getPregameHandIndex() {
        return pregameHandIndex;
    }

    public CardColor getColor() {
        return color;
    }

    public CardType getType() {
        return type;
    }
}
