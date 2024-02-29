package com.trevor.blackjack;

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

    public void clear() {
        cards.clear();
    }

    public int getScore() {
        int score = 0;
        int numAces = 0;

        for (Card card : cards) {
            score += card.getValue();
            if (card.getValue() == 11) { // If the card is an Ace
                numAces++;
            }
        }

        // Adjust score if there are Aces and the total score exceeds 21
        while (score > 21 && numAces > 0) {
            score -= 10; // Change value of Ace from 11 to 1
            numAces--;
        }

        return score;
    }

    public int getScoreWithoutHiddenCard() {
        int score = 0;

        return score += cards.get(1).getValue();
    }

    public List<Card> getCards() {
        return cards;
    }
}