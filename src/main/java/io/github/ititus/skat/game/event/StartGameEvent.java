package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

import java.util.Optional;

public class StartGameEvent extends Event {

    public StartGameEvent(ReadablePacketBuffer buf) {
        super(Type.START_GAME);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
