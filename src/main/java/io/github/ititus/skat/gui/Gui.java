package io.github.ititus.skat.gui;

import io.github.ititus.skat.SkatClient;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public abstract class Gui extends BorderPane {

    private static final KeyCodeCombination EXIT_KEYBIND = new KeyCodeCombination(KeyCode.ESCAPE);

    protected SkatClient skatClient;
    protected Gui previousGui;

    protected Gui() {
        setMinWidth(200);
        setMinHeight(200);
    }

    public SkatClient getSkatClient() {
        return skatClient;
    }

    public void setSkatClient(SkatClient skatClient) {
        this.skatClient = skatClient;
    }

    public Gui getPreviousGui() {
        return previousGui;
    }

    public void setPreviousGui(Gui previousGui) {
        this.previousGui = previousGui;
    }

    public void onOpen() {
    }

    public void close() {
        if (previousGui != null) {
            skatClient.openGui(previousGui, true);
        } else {
            skatClient.exit();
        }
    }

    public boolean isResizable() {
        return true;
    }

    protected boolean closeOnEsc() {
        return false;
    }

    public boolean onKeyPressed(KeyEvent event) {
        System.out.println("Gui.onKeyPressed in " + this + ", event=" + event);
        if (closeOnEsc() && EXIT_KEYBIND.match(event)) {
            close();
            return true;
        }

        return false;
    }

    public Gui getGuiForPrevious() {
        return this;
    }
}
