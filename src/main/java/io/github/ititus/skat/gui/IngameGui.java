package io.github.ititus.skat.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class IngameGui extends Gui {

    public IngameGui() {
        Text t = new Text("Ingame!");

        HBox hb = new HBox(t);
        hb.setAlignment(Pos.CENTER);

        setMargin(hb, new Insets(10));
        setCenter(hb);
    }
}
