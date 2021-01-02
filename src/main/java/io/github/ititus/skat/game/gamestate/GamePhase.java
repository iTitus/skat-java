package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.card.Stich;
import io.github.ititus.skat.network.NetworkEnum;

import java.util.Objects;

import static io.github.ititus.skat.SkatClient.ACTIVE_PLAYERS;
import static java.util.Arrays.stream;

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
        if (isClientOnly()) {
            throw new IllegalStateException("received client only game state from server");
        }

        byte[] activePlayersGupid = ngs.getActivePlayers();
        if (activePlayersGupid.length != ACTIVE_PLAYERS) {
            throw new IllegalArgumentException("expected " + ACTIVE_PLAYERS + " active players");
        }

        byte alleinspielerAp = ngs.getAlleinspieler();
        Player alleinspieler = null;

        Player[] activePlayers = new Player[ACTIVE_PLAYERS];
        for (byte ap = 0; ap < ACTIVE_PLAYERS; ap++) {
            Player p = c.getPlayer(activePlayersGupid[ap]);
            activePlayers[ap] = p;
            if (alleinspielerAp >= 0 && p != null && p.getActivePlayerIndex() == alleinspielerAp) {
                alleinspieler = p;
            }
        }

        if (stream(activePlayers).allMatch(Objects::isNull)) {
            activePlayers = null;
        } else if (stream(activePlayers).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("either all no activePlayers must be null");
        }

        ReizState reizState = ngs.getReizState().get(c);
        if (!reizState.isValid()) {
            reizState = null;
        }

        GameRules rules = ngs.getRules();
        if (!rules.isValid()) {
            rules = null;
        }

        ScoreBoard scoreBoard = ngs.getScoreBoard();
        if (this == SETUP) {
            scoreBoard = null;
        }

        Stich currentStich = ngs.getCurrentStich().get(c);
        Stich lastStich = ngs.getLastStich().get(c);
        short stichNum = ngs.getStichNum();
        if (!isIngamePlayingCards()) {
            currentStich = lastStich = null;
            stichNum = 0;
        }

        boolean tookSkat = ngs.didTakeSkat();
        CardCollection hand = ngs.getHand();
        if (!isInRound()) {
            tookSkat = false;
            hand = null;
        }

        Player player = Objects.requireNonNull(c.getPlayer(ngs.getMyGupid()), "my player must not be null");

        Player partner = c.getPlayer(ngs.getMyPartner());
        if (!teamsSet()) {
            partner = null;
        }

        return constructor.create(
                reizState,
                rules,
                activePlayers,
                scoreBoard,
                currentStich,
                lastStich,
                stichNum,
                alleinspieler,
                tookSkat,
                hand,
                player,
                partner
        );
    }

    private boolean isClientOnly() {
        return switch (this) {
            case CLIENT_WAIT_REIZEN_DONE, CLIENT_WAIT_STICH_DONE, CLIENT_WAIT_ANNOUNCE_SCORES, CLIENT_WAIT_ROUND_DONE -> true;
            default -> false;
        };
    }

    private boolean isIngamePlayingCards() {
        return switch (this) {
            case PLAY_STICH_C1, PLAY_STICH_C2, PLAY_STICH_C3, CLIENT_WAIT_STICH_DONE -> true;
            default -> false;
        };
    }

    private boolean isInRound() {
        return switch (this) {
            case SETUP, BETWEEN_ROUNDS -> false;
            default -> true;
        };
    }

    private boolean teamsSet() {
        return switch (this) {
            case SETUP, BETWEEN_ROUNDS, REIZEN -> false;
            default -> true;
        };
    }

    @FunctionalInterface
    public interface GameStateConstructor {

        GameState create(ReizState reizState, GameRules rules, Player[] activePlayers, ScoreBoard scoreBoard,
                         Stich currentStich, Stich lastStich, short stichNum, Player alleinspieler, boolean tookSkat,
                         CardCollection hand, Player player, Player partner);

    }
}
