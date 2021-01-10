package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.util.Precondition;

public final class ReizState {

    private final Phase phase;
    private final boolean waitingForTeller;
    private final int reizwert;
    private final Player winner;

    public ReizState(Phase phase, boolean waitingForTeller, int reizwert, Player winner) {
        this.phase = phase;
        this.waitingForTeller = waitingForTeller;
        this.reizwert = reizwert;
        this.winner = winner;
    }

    public boolean isValid() {
        return phase != null;
    }

    public enum Phase implements NetworkEnum<Phase> {

        INVALID,
        MITTELHAND_TO_VORHAND,
        HINTERHAND_TO_WINNER,
        WINNER,
        DONE;

        public static Phase fromId(byte id) {
            Phase[] values = values();

            Precondition.checkBounds(id, 0, values.length);

            return values[id];
        }

        @Override
        public byte getId() {
            int id = ordinal();
            Precondition.check(id, Byte.MAX_VALUE, Precondition.<Integer>lt());

            return (byte) id;
        }
    }
}
