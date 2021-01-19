package io.github.ititus.skat.gui;

import io.github.ititus.skat.game.Player;
import io.github.ititus.skat.game.action.MessageAction;
import io.github.ititus.skat.game.event.Event;
import io.github.ititus.skat.game.event.MessageEvent;
import io.github.ititus.skat.game.gamestate.GameState;
import io.github.ititus.skat.gui.ingame.IngameView;
import io.github.ititus.skat.gui.ingame.SetupView;
import io.github.ititus.skat.network.packet.ActionPacket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.stream.Collectors;

public class IngameGui extends Gui {

    private static final KeyCodeCombination SEND_KEYBIND = new KeyCodeCombination(KeyCode.ENTER);

    private final Bottom bottom;
    private IngameView view;

    public IngameGui() {
        openView(new SetupView());

        bottom = new Bottom();
        setBottom(bottom);
    }

    @Override
    public void onOpen() {
        refresh(skatClient.getGameState());
    }

    public void openView(IngameView view) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> openView(view));
            return;
        }

        view.setGui(this);
        setCenter(view);
        this.view = view;
    }

    public void handleEvent(Event e, GameState gameState) {
        if (e.getType() == Event.Type.MESSAGE) {
            onMessage(e.getActingPlayer(skatClient), ((MessageEvent) e).getMessage());
            return;
        }

        view.handleEvent(e, gameState);
    }

    public void refresh(GameState gameState) {
        view.refresh(gameState);
        bottom.refresh();
    }

    public void onPlayerJoin(Player player) {
        bottom.showMessage(player + " has joined the game");
        bottom.refresh();
    }

    public void onPlayerLeave(Player player) {
        bottom.showMessage(player + " has left the game");
        bottom.refresh();
    }

    public void onMessage(Player p, String message) {
        bottom.showMessage(p, message);
    }

    private class Bottom extends HBox {

        private final TextArea chat;
        private final TextField chatInput;
        private final Button sendButton;
        private final ListView<Player> playerListView;

        private Bottom() {
            BorderPane.setMargin(this, new Insets(10));

            chat = new TextArea();
            chat.setEditable(false);
            chat.setWrapText(true);

            sendButton = new Button("Send");
            sendButton.setOnAction(e -> send());

            chatInput = new TextField();
            chatInput.setOnKeyPressed(e -> {
                if (SEND_KEYBIND.match(e)) {
                    sendButton.fire();
                    e.consume();
                }
            });

            playerListView = new ListView<>();
            playerListView.setEditable(false);
            playerListView.setCellFactory(listView -> {
                ListCell<Player> cell = new ListCell<>() {

                    @Override
                    protected void updateItem(Player player, boolean empty) {
                        super.updateItem(player, empty);
                        if (!empty) {
                            setText(player.getName());
                        } else {
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
                cell.setEditable(false);
                return cell;
            });

            getChildren().addAll(playerListView, new VBox(chat, new HBox(chatInput, sendButton)));
        }

        private void refresh() {
            playerListView.setItems(FXCollections.observableList(
                    skatClient.getPlayers().stream()
                            .sorted(Player.ACTIVE_COMPARATOR)
                            .collect(Collectors.toUnmodifiableList())
            ));
        }

        private void send() {
            String message = chatInput.getText();
            if (message.isBlank()) {
                return;
            }

            chatInput.setText("");

            skatClient.getNetworkManager().sendPacket(new ActionPacket(new MessageAction(-1, message)));
        }

        private void showMessage(Player p, String message) {
            showMessage("<" + p.getName() + "> " + message);
        }

        private void showMessage(String message) {
            System.out.println("CHAT: " + message);

            if (!chat.getText().isBlank()) {
                chat.appendText("\n");
            }

            chat.appendText(message);
        }
    }
}
