module io.github.ititus.skat {
    requires ititus.commons;

    requires it.unimi.dsi.fastutil;

    requires io.netty.all;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;

    exports io.github.ititus.skat;
    exports io.github.ititus.skat.game;
    exports io.github.ititus.skat.game.action;
    exports io.github.ititus.skat.game.card;
    exports io.github.ititus.skat.game.event;
    exports io.github.ititus.skat.game.gamestate;
    exports io.github.ititus.skat.gui;
    exports io.github.ititus.skat.gui.ingame;
    exports io.github.ititus.skat.network;
    exports io.github.ititus.skat.network.buffer;
    exports io.github.ititus.skat.network.packet;
    exports io.github.ititus.skat.util;
    exports io.github.ititus.skat.util.precondition;
}
