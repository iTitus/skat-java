package io.github.ititus.skat.gui;

import io.github.ititus.skat.network.packet.TestPacket;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class JoinGui extends Gui {

    public JoinGui() {
        Text t = new Text("Connection established!");

        Button b = new Button("Send invalid packet to server");
        b.setOnAction(event -> main.getNetworkManager().sendPacket(new TestPacket()));

        VBox vb = new VBox(10, t, b);
        vb.setAlignment(Pos.CENTER);

        setMargin(vb, new Insets(10));
        setCenter(vb);
    }

    @Override
    public boolean onKeyPressed(KeyEvent event) {
        return false;
    }
}
