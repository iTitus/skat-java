package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.PacketBufferDeserializer;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;

import static io.github.ititus.precondition.Precondition.notNull;
import static io.github.ititus.precondition.Preconditions.check;

public enum ClientboundPacketType implements PacketBufferDeserializer<ClientboundPacket> {

    ERROR(1, ErrorPacket::new),
    CONFIRM_JOIN(3, ConfirmJoinPacket::new),
    CONFIRM_RESUME(5, ConfirmResumePacket::new),
    RESYNC(6, ResyncPacket::new),
    NOTIFY_JOIN(7, NotifyJoinPacket::new),
    NOTIFY_LEAVE(8, NotifyLeavePacket::new),
    EVENT(10, EventPacket::new);

    private static final Byte2ObjectMap<ClientboundPacketType> ID_MAP;

    static {
        ClientboundPacketType[] values = values();

        ID_MAP = new Byte2ObjectOpenHashMap<>(values.length);
        for (ClientboundPacketType type : values) {
            ID_MAP.put(type.id, type);
        }
    }

    private final byte id;
    private final PacketBufferDeserializer<? extends ClientboundPacket> deserializer;

    ClientboundPacketType(int id, PacketBufferDeserializer<? extends ClientboundPacket> deserializer) {
        this.id = (byte) id;
        this.deserializer = deserializer;
    }

    public static ClientboundPacketType fromId(byte id) {
        ClientboundPacketType type = ID_MAP.get(id);
        check(type, notNull());
        return type;
    }

    @Override
    public ClientboundPacket read(ReadablePacketBuffer buf) {
        return deserializer.read(buf);
    }
}
