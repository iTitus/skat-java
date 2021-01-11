package io.github.ititus.skat.network.packet;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.gamestate.NetworkGameState;
import io.github.ititus.skat.network.ConnectionState;
import io.github.ititus.skat.network.NetworkManager;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import javafx.application.Platform;

import static io.github.ititus.skat.SkatClient.MAX_PLAYERS;
import static io.github.ititus.skat.util.precondition.Precondition.equalTo;
import static io.github.ititus.skat.util.precondition.Preconditions.check;

public class ResyncPacket implements ClientboundPacket {

    private final NetworkGameState networkGameState;
    private final byte[] activePlayerIndices;
    private final String[] playerNames;

    public ResyncPacket(ReadablePacketBuffer buf) {
        networkGameState = new NetworkGameState(buf);

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
        check(ctx.channel().attr(NetworkManager.CONNECTION_STATE_KEY).get(), equalTo(ConnectionState.RESYNC));

        Byte2ObjectMap<Player> players = new Byte2ObjectOpenHashMap<>();
        for (byte gupid = 0; gupid < MAX_PLAYERS; gupid++) {
            if (playerNames[gupid] != null) {
                players.put(gupid, new Player(gupid, activePlayerIndices[gupid], playerNames[gupid]));
            }
        }

        Platform.runLater(() -> skatClient.resync(networkGameState, players));
    }

    @Override
    public ClientboundPacketType getClientboundType() {
        return ClientboundPacketType.RESYNC;
    }
}
