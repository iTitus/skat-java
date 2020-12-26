package io.github.ititus.skat;

import io.github.ititus.skat.network.NetworkClient;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Text t = new Text(10, 20, "Hello World");

        Group root = new Group(t);

        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.setTitle("Skat");
        stage.show();

        NetworkClient client = new NetworkClient();
    }
}
