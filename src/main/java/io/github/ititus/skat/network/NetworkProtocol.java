package io.github.ititus.skat.network;

import io.github.ititus.skat.network.packet.Packet;
import io.github.ititus.skat.network.packet.TestPacket;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

public class NetworkProtocol {

    private final Byte2ObjectMap<Supplier<? extends Packet>> idToPacketMap;
    private final Object2ByteMap<Class<? extends Packet>> packetToIdMap;

    public NetworkProtocol() {
        this.idToPacketMap = new Byte2ObjectOpenHashMap<>();
        this.packetToIdMap = new Object2ByteOpenHashMap<>();
        this.packetToIdMap.defaultReturnValue((byte) -1);

        register(1, TestPacket.class, TestPacket::new);
    }

    private <T extends Packet> void register(int id, Class<T> clazz, Supplier<T> constructor) {
        byte bId = (byte) id;
        if (bId == -1 || bId != id) {
            throw new IllegalArgumentException();
        }

        Objects.requireNonNull(clazz);
        Objects.requireNonNull(constructor);

        if (idToPacketMap.containsKey(bId) || packetToIdMap.containsKey(clazz)) {
            throw new IllegalArgumentException();
        }

        idToPacketMap.put(bId, constructor);
        packetToIdMap.put(clazz, bId);
    }

    public byte getId(Packet p) {
        byte id = packetToIdMap.getByte(p.getClass());
        if (id == -1) {
            throw new NoSuchElementException();
        }

        return id;
    }

    public Packet getPacket(byte id) {
        Supplier<? extends Packet> constructor = idToPacketMap.get(id);
        if (constructor == null) {
            throw new NoSuchElementException();
        }

        return constructor.get();
    }
}
