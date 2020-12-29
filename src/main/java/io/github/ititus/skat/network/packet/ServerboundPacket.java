package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.PacketBufferSerializer;

public interface ServerboundPacket extends PacketBufferSerializer {

    ServerboundPacketType getServerboundType();

}
