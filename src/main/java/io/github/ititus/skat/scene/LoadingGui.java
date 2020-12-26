package io.github.ititus.skat.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class LoadingGui extends Gui {

    public LoadingGui(String text) {
        HBox hb = new HBox(new Text(text));
        hb.setAlignment(Pos.CENTER);

        setMargin(hb, new Insets(10));
        setCenter(hb);
    }
}
