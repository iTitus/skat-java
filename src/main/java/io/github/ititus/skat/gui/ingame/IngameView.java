package io.github.ititus.skat.gui.ingame;

import io.github.ititus.skat.game.event.Event;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.gui.IngameGui;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;

public abstract class IngameView extends Group {

    protected IngameGui gui;

    protected IngameView() {
        BorderPane.setMargin(this, new Insets(10));
    }

    public void setGui(IngameGui gui) {
        this.gui = gui;
    }

    public abstract void handleEvent(Event e, GameState gameState);

    public void refresh(GameState gameState) {
    }
}
