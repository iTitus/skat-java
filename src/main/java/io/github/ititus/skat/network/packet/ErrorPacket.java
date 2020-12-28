package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class ErrorPacket extends Packet {

    private final ConnectionErrorType type;

    public ErrorPacket(ReadablePacketBuffer buf) {
        super(PacketType.ERROR);
        type = buf.readEnum(ConnectionErrorType::fromId);
    }

    public ErrorPacket(ConnectionErrorType type) {
        super(PacketType.ERROR);
        this.type = type;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        buf.writeEnum(type);
    }

    @Override
    public void handleClient() {
    }

    public enum ConnectionErrorType implements NetworkEnum<ConnectionErrorType> {

        PROTOCOL_VERSION_MISMATCH,
        NAME_TOO_LONG,
        INVALID_CONN_STATE,
        PLAYER_NAME_IN_USE,
        NO_SUCH_PLAYER_NAME,
        INCONSISTENT_SEQ_NUM,
        INVALID_PACKET_TYPE,
        TOO_MANY_PLAYERS,
        TOO_INVALID_JOIN_TIME,
        DISCONNECTED;

        public static ConnectionErrorType fromId(byte id) {
            ConnectionErrorType[] values = values();
            int ordinal = id - 1;
            if (ordinal < 0 || ordinal >= values.length) {
                throw new IndexOutOfBoundsException("id out of bounds");
            }

            return values[ordinal];
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
