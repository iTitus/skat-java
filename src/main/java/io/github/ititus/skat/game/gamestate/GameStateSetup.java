package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.game.event.StartGameEvent;

import java.util.Optional;

public class GameStateSetup extends GameState {

    @Override
    public Optional<GameState> apply(StartGameEvent e) {
        return Optional.of(new GameStateBetweenRounds());
    }
}
