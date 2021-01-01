package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.card.Stich;
import io.github.ititus.skat.network.NetworkEnum;

import java.util.Objects;

public enum GamePhase implements NetworkEnum<GamePhase> {

    SETUP(GameStateSetup::new),
    BETWEEN_ROUNDS(GameStateBetweenRounds::new),
    REIZEN(GameStateReizen::new),
    SKAT_AUFNEHMEN(GameStateSkatAufnehmen::new),
    SPIELANSAGE(GameStateSpielansage::new),
    PLAY_STICH_C1(GameStatePlayStichCard1::new),
    PLAY_STICH_C2(GameStatePlayStichCard2::new),
    PLAY_STICH_C3(GameStatePlayStichCard3::new),
    CLIENT_WAIT_REIZEN_DONE(GameStateClientWaitReizenDone::new),
    CLIENT_WAIT_STICH_DONE(GameStateClientWaitStichDone::new),
    CLIENT_WAIT_ANNOUNCE_SCORES(GameStateClientWaitAnnounceScores::new),
    CLIENT_WAIT_ROUND_DONE(GameStateClientWaitRoundDone::new);

    private final GameStateConstructor constructor;

    GamePhase(GameStateConstructor constructor) {
        this.constructor = constructor;
    }

    public static GamePhase fromId(byte id) {
        GamePhase[] values = values();
        int ordinal = id - 1;
        if (ordinal < 0 || ordinal >= values.length) {
            throw new IndexOutOfBoundsException("id out of bounds");
        }

        return values[ordinal];
    }

    @Override
    public byte getId() {
        int id = ordinal() + 1;
        if (id < 0 || id > Byte.MAX_VALUE) {
            throw new IndexOutOfBoundsException("ordinal out of bounds");
        }

        return (byte) id;
    }

    public GameState create(SkatClient c, NetworkGameState ngs) {
        byte[] activePlayersGupid = ngs.getActivePlayers();
        if (activePlayersGupid.length != 3) {
            throw new IllegalArgumentException("expected 3 active players");
        }

        Player[] activePlayers = new Player[3];
        for (byte ap = 0; ap < 3; ap++) {
            activePlayers[ap] = c.getPlayer(activePlayersGupid[ap]);
        }

        return constructor.create(
                ngs.getReizState().get(c),
                ngs.getRules(),
                activePlayers,
                ngs.getScoreBoard(),
                ngs.getCurrentStich().get(c),
                ngs.getLastStich().get(c),
                ngs.getStichNum(),
                c.getPlayer(ngs.getAlleinspieler()),
                ngs.isTookSkat(),
                ngs.getHand(),
                Objects.requireNonNull(c.getPlayer(ngs.getMyGupid()), "my player must not be null"),
                c.getPlayer(ngs.getMyPartner())
        );
    }

    @FunctionalInterface
    public interface GameStateConstructor {

        GameState create(ReizState reizState, GameRules rules, Player[] activePlayers, ScoreBoard scoreBoard,
                         Stich currentStich, Stich lastStich, short stichNum, Player alleinspieler, boolean tookSkat,
                         CardCollection hand, Player player, Player partner);

    }
}
