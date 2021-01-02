package io.github.ititus.skat.gui;

import io.github.ititus.skat.gui.ingame.IngameView;
import io.github.ititus.skat.gui.ingame.SetupView;
import javafx.geometry.Insets;

public class IngameGui extends Gui {

    public IngameGui() {
        openView(new SetupView());
    }

    public void openView(IngameView view) {
        view.setGui(this);

        setMargin(view, new Insets(10));
        setCenter(view);
    }
}
