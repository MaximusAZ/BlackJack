package controller;

import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import model.*;
import view.BlackJackMain;

public class BlackJackController {
	
    private final Deck deck = new Deck();
    private final BlackJackMain view;
    private final Player player = new Player();
    private final Player dealer = new Player();
    private final Hand playerHand = player.getHand();
    private final Hand dealerHand = dealer.getHand();
    private final Button hitButton;
    private final Button standButton;
    private final Button splitButton;
    private final Button doubleButton;
    private boolean playerStayed = false;
    private int accountBalance = 1000;
    private int currentBet = 0;

    public BlackJackController(BlackJackMain view, Button hitButton, Button standButton, Button splitButton, Button doubleButton) {
        this.view = view;
        this.hitButton = hitButton;
        this.standButton = standButton;
        this.splitButton = splitButton;
        this.doubleButton = doubleButton;
    }

    public List<Card> getPlayerCards() {
        return playerHand.getCards();
    }

    public List<Card> getDealerCards() {
        return dealerHand.getCards();
    }

    public void enableBetButton() {
        view.getBetButton().setDisable(false);
    }
    
    public void playGame() {
        resetGame();
        dealInitialCards();
        updateScoreLabels();
        enableGameButtons(); // Enable game buttons after starting a new game
        view.getBetButton().setDisable(false); // Enable bet button at the start of the game
    }

    private void enableGameButtons() {
        hitButton.setDisable(false);
        standButton.setDisable(false);
        splitButton.setDisable(false);
        doubleButton.setDisable(false);
    }

    public void hit() {
        Card card = deck.dealCard();
        playerHand.addCard(card);
        view.addPlayerCard(new Image(card.getImagePath()));
        updateScoreLabels();
        checkPlayerBust();
        
        // Check if player is not busted and hasn't stayed, then initiate dealer's turn
        if (!isPlayerBusted() && !playerStayed) {
            dealerPlay();
        }
    }

    public void stand() {
        playerStayed = true;
        dealerPlay();
    }

    private void resetGame() {
        playerHand.clear();
        dealerHand.clear();
        deck.shuffle();
        view.updateDealerScore("");
        view.updatePlayerScore("");
        view.clearDealerCards();
        view.clearPlayerCards();
    }

    private void dealerPlay() {
        // Dealer continues to draw cards until their score is 18 or higher
        while (dealerHand.calculateScore() < 18) {
            Card card = deck.dealCard();
            dealerHand.addCard(card);
            view.addDealerCard(new Image(card.getImagePath()));
        }
        updateScoreLabels();
        checkWinner(); // Check for winner after dealer's turn
    }
    
    private void dealInitialCards() {
        for (int i = 0; i < 2; i++) {
            playerHand.addCard(deck.dealCard());
            Card dealerCard = deck.dealCard();
            dealerHand.addCard(dealerCard);
            view.addDealerCard(new Image(dealerCard.getImagePath()));
        }
        view.addPlayerCard(new Image(playerHand.getCards().get(0).getImagePath()));
        view.addPlayerCard(new Image(playerHand.getCards().get(1).getImagePath()));
    }


    private void updateScoreLabels() {
        int playerScore = playerHand.calculateScore();
        int dealerScore = dealerHand.calculateScore();
        System.out.println("Player Score: " + playerScore);
        System.out.println("Dealer Score: " + dealerScore);
        view.updateDealerScore(Integer.toString(dealerScore));
        view.updatePlayerScore(Integer.toString(playerScore));
    }

    private boolean isPlayerBusted() {
        return playerHand.calculateScore() > 21;
    }

    private void checkPlayerBust() {
        if (isPlayerBusted()) {
            // Player busts, game over
            view.updatePlayerScore("BUST: You Lose!");
            view.getBetButton().setDisable(false); // Enable the bet button
            disableGameButtons();
        }
    }

    private void checkWinner() {
        int playerScore = playerHand.calculateScore();
        int dealerScore = dealerHand.calculateScore();

        if (playerScore > 21) {
            // Player busts, game over
            view.updatePlayerScore("BUST: You Lose!");
            disableGameButtons();
        } else if (playerStayed || isPlayerBusted()) {
            // Player has stayed or busted, now compare scores
            if (dealerScore > 21 || playerScore > dealerScore) {
                // Player wins
                view.updatePlayerScore("You Win!");
                accountBalance += currentBet * 2; // Add double the bet amount to the account balance
                view.getAccountBalanceLabel().setText("Account Balance: $" + accountBalance); // Update account balance label
            } else if (playerScore < dealerScore) {
                // Dealer wins
                view.updateDealerScore("DEALER WINS");
            } else {
                // It's a draw
                view.updatePlayerScore("It's a draw!");
                view.updateDealerScore("It's a draw!");
                accountBalance += currentBet; // Refund the original bet amount to the account balance
                view.getAccountBalanceLabel().setText("Account Balance: $" + accountBalance); // Update account balance label
            }
            disableGameButtons();
        }
        // Enable the bet button after someone wins, loses, or it's a draw
        view.getBetButton().setDisable(false);
    }





    private void disableGameButtons() {
        hitButton.setDisable(true);
        standButton.setDisable(true);
        splitButton.setDisable(true);
        doubleButton.setDisable(true);
    }
    
    public void placeBet(int betAmount) {
        System.out.println("Placing bet: " + betAmount); // For debugging
        if (betAmount > 0 && betAmount <= accountBalance) {
            currentBet = betAmount;
            accountBalance -= betAmount; // Subtract the bet amount from the account balance
            view.getAccountBalanceLabel().setText("Account Balance: $" + accountBalance); // Update the account balance label
            view.getBetButton().setDisable(true); // Disable bet button after placing bet
            // Now enable other game buttons
            enableGameButtons();
            // Notify the BlackJackMain class to enable the playGameButton
            view.enablePlayGameButton();
            // Clear the message label
            view.getMessageLabel().setText("");
            view.getMessageLabel().setVisible(false);
        } else if (betAmount > accountBalance) {
            view.getMessageLabel().setText("You don't have enough funds"); // Display message
            view.getMessageLabel().setVisible(true); // Make sure the message label is visible
        } else if (accountBalance <= 0) {
            view.getAccountBalanceLabel().setText("You are out of money"); // Update the account balance label
            // Disable all game components and reduce opacity of cards
            view.disableGameComponents();
        } else {
            // Handle invalid bet amount or insufficient balance
        }
    }


    private void disableAllButtons() {
        hitButton.setDisable(true);
        standButton.setDisable(true);
        splitButton.setDisable(true);
        doubleButton.setDisable(true);
        view.getBetButton().setDisable(true);
    }


    public void doubleDown() {
        if (currentBet * 2 <= accountBalance) {
            currentBet *= 2;
            accountBalance -= currentBet / 2;
            // Implement logic to handle double down action
        } else {
            // Handle insufficient balance for double down
        }
    }

    public void split() {
        // Implement logic to handle split action
    }
    
    public int getAccountBalance() {
        return accountBalance;
    }
}
