package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.card.Stich;
import io.github.ititus.skat.game.event.*;

import java.util.Optional;

public abstract class GameState {

    protected final GamePhase phase;
    protected final ReizState reizState;
    protected final GameRules rules;
    protected final Player[] activePlayers;
    protected final ScoreBoard scoreBoard;
    protected final Stich currentStich;
    protected final Stich lastStich;
    protected final byte stichNum;
    protected final Player alleinspieler;
    protected final boolean tookSkat;
    protected final CardCollection hand;
    protected final Player player;
    protected final Player partner;

    GameState(GamePhase phase, ReizState reizState, GameRules rules, Player[] activePlayers, ScoreBoard scoreBoard,
              Stich currentStich, Stich lastStich, byte stichNum, Player alleinspieler, boolean tookSkat,
              CardCollection hand, Player player, Player partner) {
        this.phase = phase;
        this.reizState = reizState;
        this.rules = rules;
        this.activePlayers = activePlayers;
        this.scoreBoard = scoreBoard;
        this.currentStich = currentStich;
        this.lastStich = lastStich;
        this.stichNum = stichNum;
        this.alleinspieler = alleinspieler;
        this.tookSkat = tookSkat;
        this.hand = hand;
        this.player = player;
        this.partner = partner;
    }

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
