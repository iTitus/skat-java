package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.PacketBufferDeserializer;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

public enum PacketType implements PacketBufferDeserializer<Packet> {

    ERROR(ErrorPacket::new),
    JOIN(JoinPacket::new),
    CONFIRM_JOIN(ConfirmJoinPacket::new),
    RESUME(ResumePacket::new),
    CONFIRM_RESUME(ConfirmResumePacket::new),
    RESYNC(ResyncPacket::new),
    NOTIFY_JOIN(NotifyJoinPacket::new),
    NOTIFY_LEAVE(NotifyLeavePacket::new),
    ACTION(ActionPacket::new),
    EVENT(EventPacket::new),
    REQUEST_RESYNC(RequestResyncPacket::new),
    CONFIRM_RESYNC(ConfirmResyncPacket::new),
    DISCONNECT(DisconnectPacket::new);

    private final PacketBufferDeserializer<? extends Packet> deserializer;

    PacketType(PacketBufferDeserializer<? extends Packet> deserializer) {
        this.deserializer = deserializer;
    }

    public static PacketType fromId(byte id) {
        PacketType[] values = values();
        int ordinal = id - 1;
        if (ordinal < 0 || ordinal >= values.length) {
            throw new IndexOutOfBoundsException("id out of bounds");
        }

        return values[ordinal];
    }

    @Override
    public Packet read(ReadablePacketBuffer buf) {
        return deserializer.read(buf);
    }

    public byte getId() {
        int id = ordinal() + 1;
        if (id < 0 || id > Byte.MAX_VALUE) {
            throw new IndexOutOfBoundsException("ordinal out of bounds");
        }

        return (byte) id;
    }
}
