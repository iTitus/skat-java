package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.card.Stich;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

public class NetworkGameState {

    private final GamePhase phase;
    private final ReizState reizState;
    private final GameRules rules;
    private final byte[] activePlayers;
    private final ScoreBoard scoreBoard;
    private final Stich currentStich;
    private final Stich lastStich;
    private final byte stichNum;
    private final byte alleinspieler;
    private final boolean tookSkat;
    private final CardCollection hand;
    private final byte myGupid;
    private final byte myActivePlayerIndex;
    private final byte myPartner;
    private final boolean istAlleinspieler;

    public NetworkGameState(ReadablePacketBuffer buf) {
        phase = buf.readEnum(GamePhase::fromId);
        reizState = new ReizState(buf);
        rules = new GameRules(buf);
        activePlayers = buf.readBytes(3);
        scoreBoard = ScoreBoard.read(buf);
        currentStich = new Stich(buf);
        lastStich = new Stich(buf);
        stichNum = buf.readByte();
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
