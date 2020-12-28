package io.github.ititus.skat.game.action;

import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.network.buffer.PacketBufferDeserializer;
import io.github.ititus.skat.network.buffer.PacketBufferSerializer;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

public abstract class Action implements PacketBufferSerializer {

    private final Type type;

    protected Action(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type implements NetworkEnum<Type>, PacketBufferDeserializer<Action> {

        READY(ReadyAction::new),
        REIZEN_NUMBER(ReizenNumberAction::new),
        REIZEN_CONFIRM(ReizenConfirmAction::new),
        REIZEN_PASSE(ReizenPasseAction::new),
        SKAT_TAKE(SkatTakeAction::new),
        SKAT_LEAVE(SkatLeaveAction::new),
        SKAT_PRESS(SkatPressAction::new),
        PLAY_CARD(PlayCardAction::new),
        CALL_GAME(CallGameAction::new);

        private final PacketBufferDeserializer<? extends Action> deserializer;

        Type(PacketBufferDeserializer<? extends Action> deserializer) {
            this.deserializer = deserializer;
        }

        public static Type fromId(byte id) {
            Type[] values = values();
            int ordinal = id - 1;
            if (ordinal < 0 || ordinal >= values.length) {
                throw new IndexOutOfBoundsException("id out of bounds");
            }

            return values[ordinal];
        }

        @Override
        public Action read(ReadablePacketBuffer buf) {
            return deserializer.read(buf);
        }

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
