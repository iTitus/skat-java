package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ReizenPasseAction extends Action {

    public ReizenPasseAction() {
        super(Type.REIZEN_PASSE);
    }

    @Override
    public void write(WritablePacketBuffer buf) {
    }
}
