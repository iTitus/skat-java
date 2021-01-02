package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.card.Stich;
import io.github.ititus.skat.game.event.StartGameEvent;

import java.util.Optional;

public class GameStateSetup extends GameState {

    GameStateSetup(ReizState reizState, GameRules rules, Player[] activePlayers, ScoreBoard scoreBoard,
                   Stich currentStich, Stich lastStich, short stichNum, Player alleinspieler, boolean tookSkat,
                   CardCollection hand, Player player, Player partner) {
        super(GamePhase.SETUP, reizState, rules, activePlayers, scoreBoard, currentStich, lastStich, stichNum,
                alleinspieler, tookSkat, hand, player, partner);
    }

    @Override
    public Optional<GameState> apply(SkatClient c, StartGameEvent e) {
        return Optional.of(new GameStateBetweenRounds(
                null,
                null,
                null,
                ScoreBoard.zero(),
                null,
                null,
                (short) 0,
                null,
                false,
                null,
                player,
                null
        ));
    }
}
