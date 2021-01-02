package io.github.ititus.skat.gui.ingame;

import io.github.ititus.skat.gui.IngameGui;
import javafx.scene.Group;

public class IngameView extends Group {

    protected IngameGui gui;

    public void setGui(IngameGui gui) {
        this.gui = gui;
    }
}
