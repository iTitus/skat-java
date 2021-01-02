package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
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
    protected final short stichNum;
    protected final Player alleinspieler;
    protected final boolean tookSkat;
    protected final CardCollection hand;
    protected final Player player;
    protected final Player partner;

    GameState(GamePhase phase, ReizState reizState, GameRules rules, Player[] activePlayers, ScoreBoard scoreBoard,
              Stich currentStich, Stich lastStich, short stichNum, Player alleinspieler, boolean tookSkat,
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

    public GamePhase getPhase() {
        return phase;
    }

    public ReizState getReizState() {
        return reizState;
    }

    public GameRules getRules() {
        return rules;
    }

    public Player[] getActivePlayers() {
        return activePlayers;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public Stich getCurrentStich() {
        return currentStich;
    }

    public Stich getLastStich() {
        return lastStich;
    }

    public short getStichNum() {
        return stichNum;
    }

    public Player getAlleinspieler() {
        return alleinspieler;
    }

    public boolean didTakeSkat() {
        return tookSkat;
    }

    public CardCollection getHand() {
        return hand;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getPartner() {
        return partner;
    }

    public Optional<GameState> apply(SkatClient c, PlayCardEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, StartGameEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, IllegalActionEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, AnnounceScoresEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, DistributeCardsEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, GameCalledEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, ReizenConfirmEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, ReizenDoneEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, ReizenNumberEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, ReizenPasseEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, RoundDoneEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, SkatLeaveEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, SkatPressEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, SkatTakeEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, StartRoundEvent e) {
        return Optional.empty();
    }

    public Optional<GameState> apply(SkatClient c, StichDoneEvent e) {
        return Optional.empty();
    }
}
