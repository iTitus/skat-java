package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.network.NetworkEnum;

import java.util.function.BiFunction;

public enum GamePhase implements NetworkEnum<GamePhase> {

    SETUP(GameStateSetup::create),
    BETWEEN_ROUNDS(GameStateBetweenRounds::create),
    REIZEN(GameStateReizen::create),
    SKAT_AUFNEHMEN(GameStateSkatAufnehmen::create),
    SPIELANSAGE(GameStateSpielansage::create),
    PLAY_STICH_C1((client, networkGameState) -> GameStatePlayStich.create(1, client, networkGameState)),
    PLAY_STICH_C2((client, networkGameState) -> GameStatePlayStich.create(2, client, networkGameState)),
    PLAY_STICH_C3((client, networkGameState) -> GameStatePlayStich.create(3, client, networkGameState)),
    CLIENT_WAIT_REIZEN_DONE(GameStateClientWaitReizenDone::create),
    CLIENT_WAIT_STICH_DONE(GameStateClientWaitStichDone::create),
    CLIENT_WAIT_ANNOUNCE_SCORES(GameStateClientWaitAnnounceScores::create),
    CLIENT_WAIT_ROUND_DONE(GameStateClientWaitRoundDone::create);

    private final BiFunction<SkatClient, NetworkGameState, GameState> constructor;

    GamePhase(BiFunction<SkatClient, NetworkGameState, GameState> constructor) {
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

    public GameState create(SkatClient client, NetworkGameState networkGameState) {
        return constructor.apply(client, networkGameState);
    }
}
