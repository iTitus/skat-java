package io.github.ititus.skat.gui;

public class ExitingGui extends LoadingGui {

    public ExitingGui() {
        super("Exiting...");
    }

    @Override
    protected boolean closeOnEsc() {
        return false;
    }
}
