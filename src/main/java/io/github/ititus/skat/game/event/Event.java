package io.github.ititus.skat.game.event;

import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.network.buffer.PacketBufferDeserializer;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import java.util.Optional;

public abstract class Event {

    private final Type type;
    private final long answerTo;

    protected Event(Type type, ReadablePacketBuffer buf) {
        this.type = type;
        answerTo = buf.readLong();
    }

    public Type getType() {
        return type;
    }

    public abstract Optional<GameState> visit(GameState g);

    public enum Type implements NetworkEnum<Type>, PacketBufferDeserializer<Event> {

        ILLEGAL_ACTION(IllegalActionEvent::new),
        START_GAME(StartGameEvent::new),
        START_ROUND(StartRoundEvent::new),
        DISTRIBUTE_CARDS(DistributeCardsEvent::new),
        REIZEN_NUMBER(ReizenNumberEvent::new),
        REIZEN_CONFIRM(ReizenConfirmEvent::new),
        REIZEN_PASSE(ReizenPasseEvent::new),
        REIZEN_DONE(ReizenDoneEvent::new),
        SKAT_TAKE(SkatTakeEvent::new),
        SKAT_LEAVE(SkatLeaveEvent::new),
        SKAT_PRESS(SkatPressEvent::new),
        PLAY_CARD(PlayCardEvent::new),
        STICH_DONE(StichDoneEvent::new),
        ANNOUNCE_SCORES(AnnounceScoresEvent::new),
        ROUND_DONE(RoundDoneEvent::new),
        GAME_CALLED(GameCalledEvent::new);

        private final PacketBufferDeserializer<? extends Event> deserializer;

        Type(PacketBufferDeserializer<? extends Event> deserializer) {
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
        public Event read(ReadablePacketBuffer buf) {
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