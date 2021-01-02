package io.github.ititus.skat.game.event;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

import static io.github.ititus.skat.SkatClient.ACTIVE_PLAYERS;

public class StartRoundEvent extends Event {

    private final byte[] activePlayers;

    public StartRoundEvent(ReadablePacketBuffer buf) {
        super(Type.START_ROUND, buf);
        activePlayers = buf.readBytes(ACTIVE_PLAYERS);
    }

    public byte[] getActivePlayers() {
        return activePlayers;
    }

    @Override
    public Optional<GameState> visit(SkatClient c, GameState g) {
        return g.apply(c, this);
    }
}
