package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.card.Stich;
import io.github.ititus.skat.game.event.StartGameEvent;

import java.util.Optional;

public class GameStateSkatAufnehmen extends GameState {

    GameStateSkatAufnehmen(GamePhase phase, ReizState reizState, GameRules rules, Player[] activePlayers,
                           ScoreBoard scoreBoard, Stich currentStich, Stich lastStich, byte stichNum,
                           Player alleinspieler,
                           boolean tookSkat, CardCollection hand, Player player, Player partner) {
        super(phase, reizState, rules, activePlayers, scoreBoard, currentStich, lastStich, stichNum, alleinspieler,
                tookSkat, hand, player, partner);
    }

    static GameState create(SkatClient client, NetworkGameState networkGameState) {
        return null;
    }

    @Override
    public Optional<GameState> apply(StartGameEvent e) {
        return Optional.empty();
    }
}
