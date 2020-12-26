package io.github.ititus.skat.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TestGui extends Gui {

    public TestGui(int index) {
        Text t = new Text("Test: " + index);

        Button b = new Button("Test");
        b.setOnAction(event -> main.openGui(new TestGui(index + 1)));

        VBox vb = new VBox(20, t, b);
        vb.setAlignment(Pos.CENTER);

        setMargin(vb, new Insets(10));
        setCenter(vb);
    }

    @Override
    public boolean isResizable() {
        return false;
    }
}
