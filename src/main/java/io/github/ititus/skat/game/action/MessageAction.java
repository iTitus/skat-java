package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class MessageAction extends Action {

    private final String message;

    public MessageAction(long id, String message) {
        super(Type.MESSAGE, id);
        this.message = message;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        super.write(buf);
        buf.writeString(message);
    }
}
