package io.github.ititus.skat.network.packet;

public enum ServerboundPacketType {

    ERROR(1),
    JOIN(2),
    RESUME(4),
    ACTION(9),
    REQUEST_RESYNC(11),
    CONFIRM_RESYNC(12),
    DISCONNECT(13);

    private final byte id;

    ServerboundPacketType(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return id;
    }
}
