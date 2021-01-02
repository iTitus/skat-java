package io.github.ititus.skat.gui.ingame;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ReizenView extends IngameView {

    public ReizenView() {
        HBox hb = new HBox(10, new Text("Reizen"));
        hb.setAlignment(Pos.CENTER);

        getChildren().add(hb);
    }
}
