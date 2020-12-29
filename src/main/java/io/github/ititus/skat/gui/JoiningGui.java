package io.github.ititus.skat.gui;

public class JoiningGui extends LoadingGui {

    private final String name;
    private final boolean resume;

    public JoiningGui(String name, boolean resume) {
        super("Joining...");
        this.name = name;
        this.resume = resume;
    }

    @Override
    public void onOpen() {
    }
}
