package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class StartRoundEvent extends Event {

    private final byte[] activePlayers;

    public StartRoundEvent(ReadablePacketBuffer buf) {
        super(Type.START_ROUND, buf);
        activePlayers = buf.readBytes(3);
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
