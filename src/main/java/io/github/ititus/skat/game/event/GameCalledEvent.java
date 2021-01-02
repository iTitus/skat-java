package io.github.ititus.skat.game.event;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.gamestate.GameRules;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public class GameCalledEvent extends Event {

    private final GameRules gameRules;

    public GameCalledEvent(ReadablePacketBuffer buf) {
        super(Type.GAME_CALLED, buf);
        gameRules = new GameRules(buf);
    }

    @Override
    public Optional<GameState> visit(SkatClient c, GameState g) {
        return g.apply(c, this);
    }
}
