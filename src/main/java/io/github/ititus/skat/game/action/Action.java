package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.network.buffer.PacketBufferSerializer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;
import io.github.ititus.skat.util.Precondition;

public abstract class Action implements PacketBufferSerializer {

    private final Type type;
    private final long id;

    protected Action(Type type, long id) {
        this.type = type;
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        buf.writeEnum(type);
        buf.writeLong(id);
    }

    public enum Type implements NetworkEnum<Type> {

        READY,
        REIZEN_NUMBER,
        REIZEN_CONFIRM,
        REIZEN_PASSE,
        SKAT_TAKE,
        SKAT_LEAVE,
        SKAT_PRESS,
        PLAY_CARD,
        CALL_GAME,
        MESSAGE;

        @Override
        public byte getId() {
            int id = ordinal() + 1;
            Precondition.checkBounds(id, 0, Byte.MAX_VALUE);

            return (byte) id;
        }
    }
}
