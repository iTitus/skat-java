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

        MITTELHAND_TO_VORHAND,
        HINTERHAND_TO_WINNER,
        WINNER,
        DONE;

        public static Phase fromId(byte id) {
            Phase[] values = values();
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
}
