package io.github.ititus.skat.gui.ingame;

import io.github.ititus.skat.game.action.ReadyAction;
import io.github.ititus.skat.game.event.Event;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.packet.ActionPacket;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class BetweenRoundsView extends IngameView {

    private final Button readyButton;

    public BetweenRoundsView() {
        readyButton = new Button("Start Round");
        readyButton.setOnAction(event -> ready());

        HBox hb = new HBox(10, readyButton);
        hb.setAlignment(Pos.CENTER);

        getChildren().add(hb);
    }

    private void ready() {
        readyButton.setDisable(false);
        gui.getSkatClient().getNetworkManager().sendPacket(new ActionPacket(new ReadyAction(-1)));
    }

    @Override
    public void handleEvent(Event e, GameState gameState) {
        //noinspection StatementWithEmptyBody
        if (e.getType() == Event.Type.START_ROUND) {
            // do nothing for now
        } else if (e.getType() == Event.Type.DISTRIBUTE_CARDS) {
            gui.openView(new ReizenView());
        } else {
            throw new IllegalStateException();
        }
    }
}
