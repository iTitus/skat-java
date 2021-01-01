package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.card.Stich;

public class GameStatePlayStichCard1 extends GameState {

    GameStatePlayStichCard1(ReizState reizState, GameRules rules, Player[] activePlayers, ScoreBoard scoreBoard,
                            Stich currentStich, Stich lastStich, short stichNum, Player alleinspieler,
                            boolean tookSkat, CardCollection hand, Player player, Player partner) {
        super(GamePhase.PLAY_STICH_C1, reizState, rules, activePlayers, scoreBoard, currentStich, lastStich, stichNum,
                alleinspieler, tookSkat, hand, player, partner);
    }
}
