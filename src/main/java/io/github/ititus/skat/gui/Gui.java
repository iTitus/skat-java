package io.github.ititus.skat.gui;

import io.github.ititus.skat.Main;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public abstract class Gui extends BorderPane {

    private static final KeyCodeCombination EXIT_KEYBIND = new KeyCodeCombination(KeyCode.ESCAPE);

    protected Main main;
    protected Gui previousGui;

    protected Gui() {
        setMinWidth(200);
        setMinHeight(200);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setPreviousGui(Gui previousGui) {
        this.previousGui = previousGui;
    }

    public void onOpen() {
    }

    public void close() {
        if (previousGui != null) {
            main.openGui(previousGui, true);
        } else {
            main.exit();
        }
    }

    public boolean isResizable() {
        return true;
    }

    public boolean onKeyPressed(KeyEvent event) {
        System.out.println("Gui.onKeyPressed in " + this + ", event=" + event);
        if (EXIT_KEYBIND.match(event)) {
            close();
            return true;
        }

        return false;
    }

    public Gui getGuiForPrevious() {
        return this;
    }
}
