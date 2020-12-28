package io.github.ititus.skat.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class LoadingGui extends Gui {

    private final Text loadingText;

    public LoadingGui(String text) {
        this.loadingText = new Text(text);

        HBox hb = new HBox(this.loadingText);
        hb.setAlignment(Pos.CENTER);

        setMargin(hb, new Insets(10));
        setCenter(hb);
    }

    public void setLoadingText(String text) {
        loadingText.setText(text);
    }

    @Override
    public Gui getGuiForPrevious() {
        return previousGui;
    }

    @Override
    public boolean isResizable() {
        return false;
    }
}
