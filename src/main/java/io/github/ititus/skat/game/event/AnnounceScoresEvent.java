package io.github.ititus.skat.game.event;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.util.Precondition;

import java.util.Optional;

import static io.github.ititus.skat.SkatClient.ACTIVE_PLAYERS;

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
        playerPoints = buf.readUnsignedBytes(ACTIVE_PLAYERS);
        playerStichCardCounts = buf.readUnsignedBytes(ACTIVE_PLAYERS);
        roundScore = buf.readLongs(ACTIVE_PLAYERS);
        spielwert = buf.readUnsignedShort();
        lossType = buf.readEnum(LossType::fromId);
        normalEnd = buf.readBoolean();
        schneider = buf.readBoolean();
        schwarz = buf.readBoolean();
    }

    @Override
    public Optional<GameState> visit(SkatClient c, GameState g) {
        return g.apply(c, this);
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
            Precondition.checkBounds(ordinal, 0, values.length + 1);

            return values[ordinal];
        }

        @Override
        public byte getId() {
            int id = ordinal() + 1;
            Precondition.checkBounds(id, 0, Byte.MAX_VALUE);

            return (byte) id;
        }
    }
}
