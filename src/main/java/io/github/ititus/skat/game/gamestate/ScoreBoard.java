package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Arrays;

import static io.github.ititus.precondition.IntPrecondition.equalTo;
import static io.github.ititus.precondition.Preconditions.check;
import static io.github.ititus.skat.SkatClient.MAX_PLAYERS;

public class ScoreBoard {

    private final long[] scores;

    private ScoreBoard(long[] scores) {
        check(scores.length, equalTo(MAX_PLAYERS));

        this.scores = scores;
    }

    public static ScoreBoard zero() {
        return new ScoreBoard(new long[MAX_PLAYERS]);
    }

    public static ScoreBoard read(ReadablePacketBuffer buf) {
        return new ScoreBoard(buf.readLongs(MAX_PLAYERS));
    }

    public ScoreBoard addScore(long... scoreDeltas) {
        check(scoreDeltas.length, equalTo(MAX_PLAYERS));

        long[] scores = Arrays.copyOf(this.scores, MAX_PLAYERS);
        for (int gupid = 0; gupid < MAX_PLAYERS; gupid++) {
            scores[gupid] += scoreDeltas[gupid];
        }

        return new ScoreBoard(scores);
    }

    public ScoreBoard addScore(int gupid, long scoreDelta) {
        long[] scores = Arrays.copyOf(this.scores, MAX_PLAYERS);
        scores[gupid] += scoreDelta;
        return new ScoreBoard(scores);
    }
}
