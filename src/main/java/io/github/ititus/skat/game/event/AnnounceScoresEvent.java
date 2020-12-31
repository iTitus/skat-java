package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class AnnounceScoresEvent extends Event {

    private final byte roundWinner;
    /**
     * indexed by active player
     */
    private final short[] playerPoints;
    /**
     * indexed by active player
     */
    private final short[] playerStichCardCounts;
    /**
     * indexed by active player
     */
    private final long[] roundScore;
    private final int spielwert;
    private final LossType lossType;
    private final boolean normalEnd;
    private final boolean schneider;
    private final boolean schwarz;

    public AnnounceScoresEvent(ReadablePacketBuffer buf) {
        super(Type.ANNOUNCE_SCORES, buf);
        roundWinner = buf.readByte();
        playerPoints = buf.readUnsignedBytes(3);
        playerStichCardCounts = buf.readUnsignedBytes(3);
        roundScore = buf.readLongs(3);
        spielwert = buf.readUnsignedShort();
        lossType = buf.readEnum(LossType::fromId);
        normalEnd = buf.readBoolean();
        schneider = buf.readBoolean();
        schwarz = buf.readBoolean();
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }

    public enum LossType implements NetworkEnum<LossType> {

        LOSS_TYPE_WON,
        LOSS_TYPE_WON_DURCHMARSCH,
        LOSS_TYPE_RAMSCH,
        LOSS_TYPE_LOST,
        LOSS_TYPE_LOST_UEBERREIZT;

        public static LossType fromId(byte id) {
            LossType[] values = values();
            int ordinal = id - 1;
            if (ordinal < 0 || ordinal >= values.length) {
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
}
