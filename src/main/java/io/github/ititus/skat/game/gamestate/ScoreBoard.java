package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.stream.IntStream;

public class ScoreBoard {

    private final long[] scores;

    private ScoreBoard(long[] scores) {
        this.scores = scores;
    }

    public static ScoreBoard zero() {
        return new ScoreBoard(new long[4]);
    }

    public static ScoreBoard read(ReadablePacketBuffer buf) {
        return new ScoreBoard(buf.readLongs(4));
    }

    public ScoreBoard addScore(long[] delta) {
        return new ScoreBoard(IntStream.range(0, scores.length).mapToLong(i -> this.scores[i] + delta[i]).toArray());
    }

    public ScoreBoard addScore(int gupid, long delta) {
        long[] delta_arr = new long[scores.length];
        delta_arr[gupid] = delta;
        return addScore(delta_arr);
    }
}
