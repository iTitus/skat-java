package io.github.ititus.skat.scene;

import javafx.scene.input.KeyEvent;

public class ExitingGui extends LoadingGui {

    public ExitingGui() {
        super("Exiting...");
    }

    @Override
    public boolean onKeyPressed(KeyEvent event) {
        return false;
    }
}
