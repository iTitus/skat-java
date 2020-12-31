package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ReizenNumberAction extends Action {

    private final int reizwert;

    public ReizenNumberAction(long id, int reizwert) {
        super(Type.REIZEN_NUMBER, id);
        this.reizwert = reizwert;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        super.write(buf);
        buf.writeUnsignedShort(reizwert);
    }
}
