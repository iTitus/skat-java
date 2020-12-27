package io.github.ititus.skat.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ConnectGui extends Gui {

    private final TextField hostField, portField;

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
        Button testButton = new Button("Test");
        testButton.setOnAction(event -> main.openGui(new TestGui(1)));
        HBox buttonBox = new HBox(10, testButton, connectButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vb = new VBox(20, inputBox1, inputBox2, buttonBox);
        vb.setAlignment(Pos.CENTER);

        setMargin(vb, new Insets(20));
        setCenter(vb);

        sceneProperty().addListener((observable, oldValue, newValue) -> connectButton.requestFocus());

        connectButton.setOnAction(event -> connect());
    }

    private void connect() {
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

        main.connect(host, port);
    }

    private void showError(String error) {
        System.out.println("ERROR: " + error);
    }
}
