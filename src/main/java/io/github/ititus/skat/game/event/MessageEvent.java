package io.github.ititus.skat.game.event;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class MessageEvent extends Event {

    private final String message;

    public MessageEvent(ReadablePacketBuffer buf) {
        super(Type.MESSAGE, buf);
        message = buf.readString();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public Optional<GameState> visit(SkatClient c, GameState g) {
        return g.apply(c, this);
    }
}
