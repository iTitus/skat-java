package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.network.NetworkEnum;

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
            if (id < 0 || id >= values.length) {
                throw new IndexOutOfBoundsException("id out of bounds");
            }

            return values[id];
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
