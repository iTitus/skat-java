package io.github.ititus.skat.gui.ingame;

import io.github.ititus.skat.game.event.Event;
import io.github.ititus.skat.game.gamestate.GameState;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ReizenView extends IngameView {

    public ReizenView() {
        Rectangle r = new Rectangle(100, 100);
        r.setFill(Color.GREEN);

        HBox hb = new HBox(10, new Text("Reizen"), r);
        hb.setAlignment(Pos.CENTER);

        getChildren().add(hb);
    }

    @Override
    public void handleEvent(Event e, GameState gameState) {
    }
}
