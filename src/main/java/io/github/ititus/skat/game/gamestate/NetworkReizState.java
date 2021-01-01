package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

public final class NetworkReizState {

    private final ReizState.Phase phase;
    private final boolean waitingForTeller;
    private final int reizwert;
    /**
     * indexed by ap
     */
    private final byte winner;

    public NetworkReizState(ReadablePacketBuffer buf) {
        phase = buf.readEnum(ReizState.Phase::fromId);
        waitingForTeller = buf.readBoolean();
        reizwert = buf.readUnsignedShort();
        winner = buf.readByte();
    }

    public ReizState get(SkatClient skatClient) {
        return new ReizState(
                phase,
                waitingForTeller,
                reizwert,
                skatClient.getPlayer(winner)
        );
    }
}
