package io.github.ititus.skat.gui;

import io.github.ititus.skat.network.packet.ErrorPacket;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class JoinGui extends Gui {

    public JoinGui() {
        Text t = new Text("Connection established!");

        setMargin(t, new Insets(10));
        setCenter(t);

        Button disconnect = new Button("Disconnect");
        disconnect.setOnAction(event -> main.disconnect("Disconnect"));

        Button testSender = new Button("Send invalid packet to server");
        testSender.setOnAction(
                event -> main.getNetworkManager()
                        .sendPacket(new ErrorPacket(ErrorPacket.ConnectionErrorType.DISCONNECTED))
        );

        HBox hb = new HBox(10, disconnect, testSender);
        hb.setAlignment(Pos.CENTER);

        setMargin(hb, new Insets(10));
        setBottom(hb);
    }

    @Override
    protected boolean closeOnEsc() {
        return false;
    }
}
