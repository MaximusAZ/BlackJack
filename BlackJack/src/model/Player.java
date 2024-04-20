package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Hand> hands;
    private int balance;

    public Player() {
        this.hands = new ArrayList<>();
        this.balance = 1000; // Starting balance
        this.hands.add(new Hand()); // Initialize at least one hand for the player
    }

    public Hand getHand() {
        return hands.get(0); // Return the first hand
    }

    public List<Hand> getHands() {
        return hands;
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

    public void clearHands() {
        hands.clear();
    }

    public void addHand(Hand hand) {
        hands.add(hand);
    }

    public void removeHand(Hand hand) {
        hands.remove(hand);
    }
}
