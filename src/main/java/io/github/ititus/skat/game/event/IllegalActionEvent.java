package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class IllegalActionEvent extends Event {

    public IllegalActionEvent(ReadablePacketBuffer buf) {
        super(Type.ILLEGAL_ACTION, buf);
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
