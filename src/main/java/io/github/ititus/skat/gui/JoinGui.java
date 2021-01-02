package io.github.ititus.skat.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class JoinGui extends Gui {

    private final TextField nameField;
    private final CheckBox resumeBox;
    private final Button join;

    public JoinGui() {
        Text t = new Text("Connection established!");
        HBox topBox = new HBox(10, t);
        topBox.setAlignment(Pos.CENTER);

        setMargin(topBox, new Insets(10));
        setTop(topBox);

        Text label1 = new Text("Name");
        nameField = new TextField("iTitus");
        nameField.setPromptText("Name");
        HBox inputBox1 = new HBox(10, label1, nameField);
        inputBox1.setAlignment(Pos.CENTER);

        resumeBox = new CheckBox("Resume");
        HBox inputBox2 = new HBox(10, resumeBox);
        inputBox2.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox(10, inputBox1, inputBox2);
        setMargin(centerBox, new Insets(10));
        setCenter(centerBox);

        Button disconnect = new Button("Disconnect");
        disconnect.setOnAction(event -> skatClient.disconnect("Disconnect"));

        join = new Button("Join");
        join.setDefaultButton(true);
        join.setOnAction(event -> join());

        HBox bottomBox = new HBox(10, disconnect, join);
        bottomBox.setAlignment(Pos.CENTER);

        setMargin(bottomBox, new Insets(10));
        setBottom(bottomBox);
    }

    private void join() {
        String name = nameField.getText();
        boolean resume = resumeBox.isSelected();

        skatClient.openGui(new JoiningGui(name, resume));
    }

    @Override
    public void onOpen() {
        join.requestFocus();
    }
}
