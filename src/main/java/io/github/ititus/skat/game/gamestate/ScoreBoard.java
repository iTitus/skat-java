package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.util.Precondition;

import java.util.Arrays;

import static io.github.ititus.skat.SkatClient.MAX_PLAYERS;

public class ScoreBoard {

    private final long[] scores;

    private ScoreBoard(long[] scores) {
        Precondition.checkEq(scores.length, MAX_PLAYERS);

        this.scores = scores;
    }

    public static ScoreBoard zero() {
        return new ScoreBoard(new long[MAX_PLAYERS]);
    }

    public static ScoreBoard read(ReadablePacketBuffer buf) {
        return new ScoreBoard(buf.readLongs(MAX_PLAYERS));
    }

    public ScoreBoard addScore(long... scoreDeltas) {
        Precondition.checkEq(scoreDeltas.length, MAX_PLAYERS);

        long[] scores = Arrays.copyOf(this.scores, SkatClient.MAX_PLAYERS);
        for (int gupid = 0; gupid < MAX_PLAYERS; gupid++) {
            scores[gupid] += scoreDeltas[gupid];
        }

        return new ScoreBoard(scores);
    }

    public ScoreBoard addScore(int gupid, long scoreDelta) {
        long[] scores = Arrays.copyOf(this.scores, SkatClient.MAX_PLAYERS);
        scores[gupid] += scoreDelta;
        return new ScoreBoard(scores);
    }
}
