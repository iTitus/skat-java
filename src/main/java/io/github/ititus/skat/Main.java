package io.github.ititus.skat;

import io.github.ititus.skat.scene.ConnectGui;
import io.github.ititus.skat.scene.Gui;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        openGui(new ConnectGui(), true);

        primaryStage.setTitle("Skat");
        primaryStage.show();
    }

    public void openGui(Gui gui) {
        openGui(gui, false);
    }

    public void openGui(Gui gui, boolean replace) {
        gui.setMain(this);

        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(gui);
            stage.setScene(scene);
        } else {
            Parent oldRoot = scene.getRoot();
            if (oldRoot instanceof Gui) {
                Gui oldGui = (Gui) oldRoot;
                if (!replace) {
                    gui.setPreviousGui(oldGui);
                }
            }

            scene.setRoot(gui);
        }

        stage.sizeToScene();
        stage.setResizable(gui.isResizable());
    }

    public void exit() {
        Platform.exit();
    }

    public Stage getStage() {
        return stage;
    }
}
