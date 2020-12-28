package io.github.ititus.skat.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ConnectGui extends Gui {

    private final TextField hostField, portField;
    private final Text errorText;

    public ConnectGui() {
        Text label1 = new Text("Host");
        hostField = new TextField("localhost");
        hostField.setPromptText("Host");
        HBox inputBox1 = new HBox(10, label1, hostField);
        inputBox1.setAlignment(Pos.CENTER);

        Text label2 = new Text("Port");
        portField = new TextField("55555");
        portField.setPromptText("Port");
        HBox inputBox2 = new HBox(10, label2, portField);
        inputBox2.setAlignment(Pos.CENTER);

        Button connectButton = new Button("Connect");
        connectButton.setDefaultButton(true);
        HBox buttonBox = new HBox(10, connectButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vb = new VBox(20, inputBox1, inputBox2, buttonBox);
        vb.setAlignment(Pos.CENTER);

        setMargin(vb, new Insets(20));
        setCenter(vb);

        errorText = new Text();
        errorText.setFill(Color.RED);
        errorText.setVisible(false);

        HBox hb = new HBox(errorText);
        hb.setAlignment(Pos.CENTER);

        setMargin(hb, new Insets(10));
        setBottom(hb);

        sceneProperty().addListener((observable, oldValue, newValue) -> connectButton.requestFocus());

        connectButton.setOnAction(event -> connect());
    }

    private void connect() {
        hideError();

        String host = hostField.getText();
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException ignored) {
            showError("Port is not a valid number");
            return;
        }

        if (port <= 0) {
            showError("Port must be positive");
            return;
        }

        main.openGui(new ConnectingGui(host, port));
    }

    public void hideError() {
        errorText.setVisible(false);
    }

    public void showError(String error) {
        errorText.setText(error);
        errorText.setVisible(true);
    }
}
