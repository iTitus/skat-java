package io.github.ititus.skat.game.action;

import io.github.ititus.skat.game.gamestate.GameRules;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class CallGameAction extends Action {

    private final GameRules gameRules;

    public CallGameAction(long id, GameRules gameRules) {
        super(Type.CALL_GAME, id);
        this.gameRules = gameRules;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        super.write(buf);
        gameRules.write(buf);
    }
}
