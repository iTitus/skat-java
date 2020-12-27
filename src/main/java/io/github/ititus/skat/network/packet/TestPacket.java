package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.github.ititus.skat.network.buffer.WritablePacketBuffer;

public class TestPacket implements Packet {

    private String content;

    @Override
    public void read(ReadablePacketBuffer buf) {
        while (buf.readableBytes() > 0) {
            buf.readByte();
        }

        content = buf.dump();
    }

    @Override
    public void write(WritablePacketBuffer buf) {
        buf.writeByte((byte) 0);
    }

    public String getContent() {
        return content;
    }
}
