package io.github.ititus.skat;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vb = new VBox();
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        Text t = new Text("Hello World");
        vb.getChildren().add(t);

        Scene scene = new Scene(vb, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Skat");
        primaryStage.show();
    }
}
