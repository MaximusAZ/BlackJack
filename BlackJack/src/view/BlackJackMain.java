package view;

import controller.BlackJackController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BlackJackMain extends Application {
    private final Label dealerScoreLabel = new Label();
    private final Label playerScoreLabel = new Label();
    private final Label accountBalanceLabel = new Label("Account Balance: $1000");
    private final HBox dealerCardsBox = new HBox();
    private final HBox playerCardsBox = new HBox();
    private final Button playGameButton = new Button("Play Game");
    private final Button hitButton = new Button("Hit");
    private final Button standButton = new Button("Stand");
    private final Button splitButton = new Button("Split");
    private final Button doubleButton = new Button("Double");
    private final TextField betTextField = new TextField();
    private final Button betButton = new Button("Bet");
    private int accountBalance = 1000;
    private final Label messageLabel = new Label();
    private final BlackJackController controller = new BlackJackController(this, hitButton, standButton, splitButton, doubleButton);


    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: green;");
        root.setPadding(new Insets(20)); 
        betTextField.setPrefWidth(60);
        betTextField.setText("$");
        playGameButton.setDisable(true);
        messageLabel.setVisible(false);

        HBox topBox = new HBox(playGameButton, betTextField, betButton, accountBalanceLabel, messageLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setSpacing(10);

        // Middle box containing play game button and dealer score label
        HBox middleBox = new HBox(dealerScoreLabel);
        middleBox.setAlignment(Pos.CENTER);
        middleBox.setSpacing(10);
        
        // Set spacing between cards in playerCardsBox and dealerCardsBox
        playerCardsBox.setSpacing(10);
        dealerCardsBox.setSpacing(10); 

        // VBox containing dealer and player cards
        VBox cardsBox = new VBox(dealerCardsBox, playerCardsBox);
        cardsBox.setAlignment(Pos.CENTER);
        cardsBox.setSpacing(10);

        // Bottom box containing game buttons and player score label
        HBox bottomBox = new HBox(hitButton, standButton, splitButton, doubleButton, playerScoreLabel);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(10);
        
        dealerScoreLabel.setPadding(new Insets(10));

        // Create a VBox to hold both middle and cards boxes
        VBox middleAndCardsBox = new VBox(middleBox, cardsBox);

        // Add all boxes to the root BorderPane
        root.setTop(topBox);
        root.setCenter(middleAndCardsBox);
        root.setBottom(bottomBox);
 
        // Add an event handler to the hit button
        hitButton.setOnAction(event -> {
            controller.hit();
            clearMessageLabelIfNeeded();
        });

        // Add an event handler to the stand button
        standButton.setOnAction(event -> {
            controller.stand();
            clearMessageLabelIfNeeded();
        });

        // Add an event handler to the double button
        doubleButton.setOnAction(event -> {
            controller.doubleDown();
            clearMessageLabelIfNeeded();
        });

        // Add an event handler to the bet button
        betButton.setOnAction(event -> {
            String betAmountText = betTextField.getText().replaceAll("\\$", ""); 
            int betAmount = Integer.parseInt(betAmountText);
            controller.placeBet(betAmount);
            clearMessageLabelIfNeeded();
        });
        splitButton.setOnAction(event -> controller.split());
     // Add an event handler to the play game button
        playGameButton.setOnAction(event -> {
            controller.playGame();
            betButton.setDisable(true); // Disable bet button after clicking play game
            clearMessageLabelIfNeeded();
        });

        // Create the GUI scene
        Scene scene = new Scene(root, 650, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("BlackJack Game");
        primaryStage.show();
    }
    
    public void disableGameComponents() {
        hitButton.setDisable(true);
        standButton.setDisable(true);
        splitButton.setDisable(true);
        doubleButton.setDisable(true);
        playGameButton.setDisable(true);
        betButton.setDisable(true);
        for (Node card : dealerCardsBox.getChildren()) {
            card.setOpacity(0.3); // This reduces opacity of cards when the player runs out of money to hint at the game being over
        }
        for (Node card : playerCardsBox.getChildren()) {
            card.setOpacity(0.3); 
        }
    }

    public void enableGameComponents() {
        hitButton.setDisable(false);
        standButton.setDisable(false);
        splitButton.setDisable(false);
        doubleButton.setDisable(false);
        playGameButton.setDisable(false);
        betButton.setDisable(false);
        for (Node card : dealerCardsBox.getChildren()) {
            card.setOpacity(1.0); // Restore opacity 
        }
        for (Node card : playerCardsBox.getChildren()) {
            card.setOpacity(1.0); 
        }
    }


    public void updateDealerScore(String score) {
        dealerScoreLabel.setText("Dealer Score: " + score);
    }

    public void updatePlayerScore(String score) {
        playerScoreLabel.setText("Player Score: " + score);
    }

    public void addDealerCard(Image image) {
        if (dealerCardsBox.getChildren().size() < 5) { // Limit to 5 cards in window, might make it 6 bc there was a case i hit until 6 cards
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            dealerCardsBox.getChildren().add(imageView); 
            System.out.println("Dealer's Card Drawn: " + image.getUrl()); 
        }
    }

    public void addPlayerCard(Image image) {
        if (playerCardsBox.getChildren().size() < 5) { 
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            playerCardsBox.getChildren().add(imageView);
            System.out.println("Player's Card Drawn: " + image.getUrl());
        }
    }
    
    private void clearMessageLabelIfNeeded() {
        if (messageLabel.isVisible()) {
            messageLabel.setText("");
            messageLabel.setVisible(false);
        }
    }

    public void clearDealerCards() {
        dealerCardsBox.getChildren().clear();
    }

    public void clearPlayerCards() {
        playerCardsBox.getChildren().clear();
    }
    
    public Button getHitButton() {
        return hitButton;
    }

    public Button getStandButton() {
        return standButton;
    }
    
    public Button getBetButton() {
        return betButton;
    }
 
    public Button getDoubleButton() {
        return doubleButton;
    }

    public Button getSplitButton() {
        return splitButton;
    }

    public TextField getBetTextField() {
        return betTextField;
    }
    
    public Label getAccountBalanceLabel() {
        return accountBalanceLabel;
    }
    
    public void enablePlayGameButton() {
        playGameButton.setDisable(false);
    }
    
    public Button getPlayGameButton() {
        return playGameButton;
    }
    
    public Label getMessageLabel() {
    	return messageLabel;
    }
    
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
