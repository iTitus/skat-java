package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.game.card.CardColor;
import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.network.buffer.PacketBufferSerializer;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public final class GameRules implements PacketBufferSerializer {

    private final GameType type;
    private final CardColor trumpf;
    private final boolean hand;
    private final boolean schneiderAngesagt;
    private final boolean schwarzAngesagt;
    private final boolean ouvert;

    public GameRules(ReadablePacketBuffer buf) {
        type = buf.readNullableEnum(GameType::fromId);
        trumpf = buf.readNullableEnum(CardColor::fromId);
        hand = buf.readBoolean();
        schneiderAngesagt = buf.readBoolean();
        schwarzAngesagt = buf.readBoolean();
        ouvert = buf.readBoolean();
    }

    public boolean isValid() {
        return type != null;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        buf.writeNullableEnum(type);
        buf.writeNullableEnum(trumpf);
        buf.writeBoolean(hand);
        buf.writeBoolean(schneiderAngesagt);
        buf.writeBoolean(schwarzAngesagt);
        buf.writeBoolean(ouvert);
    }

    public enum GameType implements NetworkEnum<GameType> {

        COLOR,
        GRAND,
        NULL,
        RAMSCH;

        public static GameType fromId(byte id) {
            GameType[] values = values();
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
            int id = ordinal();
            if (id > Byte.MAX_VALUE) {
                throw new IndexOutOfBoundsException("ordinal out of bounds");
            }

            return (byte) id;
        }
    }
}
