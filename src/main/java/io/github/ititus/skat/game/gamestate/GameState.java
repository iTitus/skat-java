package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.game.event.*;

import java.util.Optional;

public abstract class GameState {

    public Optional<GameState> apply(PlayCardEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(StartGameEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(IllegalActionEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(AnnounceScoresEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(DistributeCardsEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(GameCalledEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(ReizenConfirmEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(ReizenDoneEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(ReizenNumberEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(ReizenPasseEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(RoundDoneEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatLeaveEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatPressEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatTakeEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(StartRoundEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(StichDoneEvent e) {
        return Optional.empty();
    }
}
