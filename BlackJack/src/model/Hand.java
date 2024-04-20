package model;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int calculateScore() {
        int score = 0;
        int numberOfAces = 0;

        for (Card card : cards) {
            Rank rank = card.getRank();
            if (rank == Rank.ACE) {
                numberOfAces++;
                score += 11; // Aces initially count as 11
            } else if (rank == Rank.JACK || rank == Rank.QUEEN || rank == Rank.KING) {
                score += 10; // Jack, Queen, and King all have a value of 10
            } else {
                score += rank.getValue();
            }
        }

        // Adjust score for aces if needed
        while (score > 21 && numberOfAces > 0) {
            score -= 10; // Change ace value from 11 to 1
            numberOfAces--;
        }

        return score;
    }
    
    public boolean isBlackjack() {
        return cards.size() == 2 && calculateScore() == 21;
    }
    
    public List<Card> getCards() {
        return cards;
    }
    
    public void clear() {
        cards.clear();
    }
}
