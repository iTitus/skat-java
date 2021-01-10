package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.network.NetworkEnum;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;
import io.github.ititus.skat.util.Precondition;
import io.netty.channel.ChannelHandlerContext;

public class ErrorPacket implements ClientboundPacket, ServerboundPacket {

    private final ConnectionErrorType type;

    public ErrorPacket(ReadablePacketBuffer buf) {
        type = buf.readEnum(ConnectionErrorType::fromId);
    }

    public ErrorPacket(ConnectionErrorType type) {
        this.type = type;
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        buf.writeEnum(type);
    }

    @Override
    public void handle(ChannelHandlerContext ctx, SkatClient skatClient) {
        skatClient.disconnect("Error: " + type);
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.ERROR;
    }

    @Override
    public ServerboundPacketType getServerboundType() {
        return ServerboundPacketType.ERROR;
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
            Precondition.checkBounds(ordinal, 0, values.length);

            return values[ordinal];
        }

        @Override
        public byte getId() {
            int id = ordinal() + 1;
            Precondition.checkBoundsI(id, 0, Byte.MAX_VALUE);

            return (byte) id;
        }
    }
}
