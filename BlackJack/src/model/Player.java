package model;

public class Player {
    private final Hand hand;
    private int balance;

    public Player() {
        this.hand = new Hand();
        this.balance = 100; // Starting balance
    }

    public Hand getHand() {
        return hand;
    }

    public int getBalance() {
        return balance;
    }

    public void addToBalance(int amount) {
        balance += amount;
    }

    public void removeFromBalance(int amount) {
        balance -= amount;
    }
    
    public void clearHand() {
        hand.clear();
    }

    // Implement additional methods for player actions like hitting, standing, etc.
}
