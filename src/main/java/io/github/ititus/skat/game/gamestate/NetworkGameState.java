package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.card.NetworkStich;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

public class NetworkGameState {

    private final GamePhase phase;
    private final NetworkReizState reizState;
    private final GameRules rules;
    private final byte[] activePlayers;
    private final ScoreBoard scoreBoard;
    private final NetworkStich currentStich;
    private final NetworkStich lastStich;
    private final short stichNum;
    private final byte alleinspieler;
    private final boolean tookSkat;
    private final CardCollection hand;
    private final byte myGupid;
    private final byte myActivePlayerIndex;
    private final byte myPartner;
    private final boolean istAlleinspieler;

    public NetworkGameState(ReadablePacketBuffer buf) {
        phase = buf.readEnum(GamePhase::fromId);
        reizState = new NetworkReizState(buf);
        rules = new GameRules(buf);
        activePlayers = buf.readBytes(3);
        scoreBoard = ScoreBoard.read(buf);
        currentStich = new NetworkStich(buf);
        lastStich = new NetworkStich(buf);
        stichNum = buf.readUnsignedByte();
        alleinspieler = buf.readByte();
        tookSkat = buf.readBoolean();
        hand = CardCollection.read(buf);
        myGupid = buf.readByte();
        myActivePlayerIndex = buf.readByte();
        myPartner = buf.readByte();
        istAlleinspieler = buf.readBoolean();
    }

    public GameState get(SkatClient client) {
        return phase.create(client, this);
    }
}
