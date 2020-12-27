package io.github.ititus.skat.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class JoinGui extends Gui {

    public JoinGui() {
        HBox hb = new HBox(new Text("Connection established!"));
        hb.setAlignment(Pos.CENTER);

        setMargin(hb, new Insets(10));
        setCenter(hb);
    }
}
