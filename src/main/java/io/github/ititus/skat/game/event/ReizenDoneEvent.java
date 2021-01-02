package io.github.ititus.skat.game.event;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class ReizenDoneEvent extends Event {

    private final byte alleinspieler;

    public ReizenDoneEvent(ReadablePacketBuffer buf) {
        super(Type.REIZEN_DONE, buf);
        alleinspieler = buf.readByte();
    }

    @Override
    public Optional<GameState> visit(SkatClient c, GameState g) {
        return g.apply(c, this);
    }
}
