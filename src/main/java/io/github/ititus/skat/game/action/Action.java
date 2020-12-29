package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.network.buffer.PacketBufferSerializer;

public abstract class Action implements PacketBufferSerializer {

    private final Type type;

    protected Action(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
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
        CALL_GAME;

        @Override
        public byte getId() {
            int id = ordinal() + 1;
            if (id < 0 || id > Byte.MAX_VALUE) {
                throw new IndexOutOfBoundsException("ordinal out of bounds");
            }

            return (byte) id;
        }
    }
}
