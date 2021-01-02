package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.game.gamestate.NetworkGameState;
import io.github.ititus.skat.network.ConnectionState;
import io.github.ititus.skat.network.NetworkManager;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import javafx.application.Platform;

import static io.github.ititus.skat.SkatClient.MAX_PLAYERS;

public class ResyncPacket implements ClientboundPacket {

    private final NetworkGameState gameState;
    private final byte[] activePlayerIndices;
    private final String[] playerNames;

    public ResyncPacket(ReadablePacketBuffer buf) {
        gameState = new NetworkGameState(buf);

        activePlayerIndices = new byte[MAX_PLAYERS];
        playerNames = new String[MAX_PLAYERS];
        for (byte gupid = 0; gupid < MAX_PLAYERS; gupid++) {
            if (buf.readBoolean()) {
                activePlayerIndices[gupid] = buf.readByte();
                playerNames[gupid] = buf.readString();
            } else {
                activePlayerIndices[gupid] = -1;
                playerNames[gupid] = null;
            }
        }
    }

    @Override
    public void handle(ChannelHandlerContext ctx, SkatClient skatClient) {
        if (ctx.channel().attr(NetworkManager.CONNECTION_STATE_KEY).get() != ConnectionState.RESYNC) {
            throw new IllegalStateException("expected connection state resync");
        }

        GameState state = gameState.get(skatClient);

        Byte2ObjectMap<Player> players = new Byte2ObjectOpenHashMap<>();
        for (byte gupid = 0; gupid < MAX_PLAYERS; gupid++) {
            if (playerNames[gupid] != null) {
                players.put(gupid, new Player(gupid, activePlayerIndices[gupid], playerNames[gupid]));
            }
        }

        Platform.runLater(() -> skatClient.resync(state, players));
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.RESYNC;
    }
}
