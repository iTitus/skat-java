package io.github.ititus.skat.gui.ingame;

import io.github.ititus.skat.game.action.ReadyAction;
import io.github.ititus.skat.game.event.Event;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.network.packet.ActionPacket;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class SetupView extends IngameView {

    private final Button readyButton;

    public SetupView() {
        readyButton = new Button("Start Game");
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
        if (e.getType() != Event.Type.START_GAME) {
            throw new IllegalStateException();
        }

        gui.openView(new BetweenRoundsView());
    }
}
