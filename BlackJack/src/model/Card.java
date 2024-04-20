package model;

public class Card {
	
    private final Rank rank;
    private final Suit suit;
    private final String imagePath;

    public Card(Rank rank, Suit suit, String imagePath) {
        this.rank = rank;
        this.suit = suit;
        this.imagePath = imagePath;
        //System.out.println("Generated image path: " + imagePath); // Print the generated image path
    }

    public Card(Rank rank, Suit suit) {
        this(rank, suit, "cards/" + rank.toString() + "_of_" + suit.toString() + ".png");
    }

    // Getters for rank, suit, and imagePath
    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public String getImagePath() {
        return imagePath;
    }
}
