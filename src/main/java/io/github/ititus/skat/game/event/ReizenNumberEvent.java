package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class ReizenNumberEvent extends Event {

    public ReizenNumberEvent(ReadablePacketBuffer buf) {
        super(Type.REIZEN_NUMBER);
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
