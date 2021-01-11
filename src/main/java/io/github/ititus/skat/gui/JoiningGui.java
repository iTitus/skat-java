package io.github.ititus.skat.gui;

import io.github.ititus.skat.network.packet.JoinPacket;
import io.github.ititus.skat.network.packet.ResumePacket;

import static io.github.ititus.skat.util.precondition.BooleanPrecondition.isFalse;
import static io.github.ititus.skat.util.precondition.BooleanPrecondition.isTrue;
import static io.github.ititus.skat.util.precondition.Preconditions.check;

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
        skatClient.getNetworkManager().sendPacket(resume ? new ResumePacket(name) : new JoinPacket(name));
    }

    public void confirmJoin(byte gupid) {
        check(resume, isFalse());

        confirm(gupid);
    }

    public void confirmResume(byte gupid) {
        check(resume, isTrue());

        confirm(gupid);
    }

    private void confirm(byte gupid) {
        skatClient.setup(gupid, name);
        skatClient.startResync();
    }
}
