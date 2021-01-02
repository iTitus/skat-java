package io.github.ititus.skat.game.gamestate;

import io.github.ititus.skat.SkatClient;
import io.github.ititus.skat.game.card.CardCollection;
import io.github.ititus.skat.game.card.NetworkStich;
import io.github.ititus.skat.network.buffer.ReadablePacketBuffer;

import static io.github.ititus.skat.SkatClient.ACTIVE_PLAYERS;

public class NetworkGameState {

    private final GamePhase phase;
    private final NetworkReizState reizState;
    private final GameRules rules;
    /**
     * map active player -> gupid
     */
    private final byte[] activePlayers;
    private final ScoreBoard scoreBoard;
    private final NetworkStich currentStich;
    private final NetworkStich lastStich;
    private final short stichNum;
    /**
     * indexed by active player
     */
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
        activePlayers = buf.readBytes(ACTIVE_PLAYERS);
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

    public GamePhase getPhase() {
        return phase;
    }

    public NetworkReizState getReizState() {
        return reizState;
    }

    public GameRules getRules() {
        return rules;
    }

    public byte[] getActivePlayers() {
        return activePlayers;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public NetworkStich getCurrentStich() {
        return currentStich;
    }

    public NetworkStich getLastStich() {
        return lastStich;
    }

    public short getStichNum() {
        return stichNum;
    }

    public byte getAlleinspieler() {
        return alleinspieler;
    }

    public boolean didTakeSkat() {
        return tookSkat;
    }

    public CardCollection getHand() {
        return hand;
    }

    public byte getMyGupid() {
        return myGupid;
    }

    public byte getMyActivePlayerIndex() {
        return myActivePlayerIndex;
    }

    public byte getMyPartner() {
        return myPartner;
    }

    public boolean isAlleinspieler() {
        return istAlleinspieler;
    }
}
