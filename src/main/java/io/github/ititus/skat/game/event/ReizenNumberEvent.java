package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class ReizenNumberEvent extends Event {

    private final int reizwertFinal;

    public ReizenNumberEvent(ReadablePacketBuffer buf) {
        super(Type.REIZEN_NUMBER, buf);
        reizwertFinal = buf.readUnsignedShort();
    }

    @Override
    public Optional<GameState> visit(GameState g) {
        return g.apply(this);
    }
}
