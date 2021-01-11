package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.card.Stich;
import io.github.ititus.skat.game.event.DistributeCardsEvent;
import io.github.ititus.skat.game.event.StartRoundEvent;

import java.util.Optional;

import static io.github.ititus.skat.SkatClient.ACTIVE_PLAYERS;
import static io.github.ititus.skat.util.precondition.Precondition.isNull;
import static io.github.ititus.skat.util.precondition.Precondition.notNull;
import static io.github.ititus.skat.util.precondition.Preconditions.check;

public class GameStateBetweenRounds extends GameState {

    GameStateBetweenRounds(ReizState reizState, GameRules rules, Player[] activePlayers, ScoreBoard scoreBoard,
                           Stich currentStich, Stich lastStich, short stichNum, Player alleinspieler, boolean tookSkat,
                           CardCollection hand, Player player, Player partner) {
        super(GamePhase.BETWEEN_ROUNDS, reizState, rules, activePlayers, scoreBoard, currentStich, lastStich,
                stichNum, alleinspieler, tookSkat, hand, player, partner);
    }

    @Override
    public Optional<GameState> apply(SkatClient c, StartRoundEvent e) {
        check(activePlayers, isNull());
        check(hand, isNull());

        byte[] activePlayersGupid = e.getActivePlayers();
        c.setActivePlayerIndices(activePlayersGupid);

        Player[] activePlayers = new Player[ACTIVE_PLAYERS];
        for (byte ap = 0; ap < ACTIVE_PLAYERS; ap++) {
            activePlayers[ap] = c.getPlayer(activePlayersGupid[ap]);
        }

        return Optional.of(new GameStateBetweenRounds(
                null,
                null,
                activePlayers,
                scoreBoard,
                null,
                null,
                (short) 0,
                null,
                false,
                null,
                c.getPlayer(player.getGupid()),
                null
        ));
    }

    @Override
    public Optional<GameState> apply(SkatClient c, DistributeCardsEvent e) {
        check(activePlayers, notNull());
        check(hand, isNull());

        return Optional.of(new GameStateReizen(
                new ReizState(
                        ReizState.Phase.MITTELHAND_TO_VORHAND,
                        true,
                        0,
                        null
                ),
                null,
                activePlayers,
                scoreBoard,
                null,
                null,
                (short) 0,
                null,
                false,
                e.getHand(),
                c.getPlayer(player.getGupid()),
                null
        ));
    }
}
