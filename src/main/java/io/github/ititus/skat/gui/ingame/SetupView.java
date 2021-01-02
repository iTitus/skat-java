package io.github.ititus.skat.gui.ingame;

import io.github.ititus.skat.game.action.ReadyAction;
import io.github.ititus.skat.network.packet.ActionPacket;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class SetupView extends IngameView {

    public SetupView() {
        Button ready = new Button("Start Game");
        ready.setOnAction(event -> ready());

        HBox hb = new HBox(10, ready);
        hb.setAlignment(Pos.CENTER);

        getChildren().add(hb);
    }

    private void ready() {
        gui.getSkatClient().getNetworkManager().sendPacket(new ActionPacket(new ReadyAction(-1)));
    }
}
