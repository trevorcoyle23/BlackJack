package com.trevor.blackjack;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<Card> deck;
    private Hand playerHand;
    private Hand dealerHand;
    private int playerScore;
    private int dealerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button dealButton = findViewById(R.id.dealButton);
        Button hitButton = findViewById(R.id.hitButton);
        Button standButton = findViewById(R.id.standButton);

        TextView winner = findViewById(R.id.winner);

        dealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
                winner.setText("");
            }
        });

        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hit();
            }
        });

        standButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stand();
            }
        });

        // Initialize the deck and hands
        deck = new ArrayList<>();
        playerHand = new Hand();
        dealerHand = new Hand();
        initializeDeck();
    }

    private void startNewGame() {
        // Clear hands
        playerHand.clear();
        dealerHand.clear();
        playerScore = 0;
        dealerScore = 0;
        ImageView dealerShownCard = findViewById(R.id.dealerShownCard);
        ImageView dealerHiddenCard = findViewById(R.id.dealerHiddenCard);
        ImageView playerCard1 = findViewById(R.id.playerCard1);
        ImageView playerCard2 = findViewById(R.id.playerCard2);

        // Deal initial cards
        dealCard(playerHand, playerCard1);
        dealCard(dealerHand, dealerShownCard);
        dealCard(playerHand, playerCard2);
        dealHiddenCard(dealerHand, dealerHiddenCard);

        // Calculate scores
        playerScore = playerHand.getScore();
        dealerScore = dealerHand.getScoreWithoutHiddenCard();

        // Update score text views
        updatePlayerScore();
        updateDealerScore();
    }

    private void hit() {
        dealCard(playerHand, null);
        playerScore = playerHand.getScore();
        updatePlayerScore();

        if (playerScore > 21) {
            endGame("Player busts! Dealer wins!");
        }
    }

    private void stand() {
        // Reveal dealer's hidden card
        ImageView dealerHiddenCard = findViewById(R.id.dealerHiddenCard);
        dealerHiddenCard.setImageResource(
                getResources().getIdentifier(
                        dealerHand.getCards().get(1).getImageFileName(),
                        "drawable",
                        getPackageName()
                )
        );
        updateDealerScore();

        // Dealer draws cards until score reaches 17 or higher
        while (dealerScore < 17) {
            dealCard(dealerHand, null);
            dealerScore = dealerHand.getScoreWithoutHiddenCard();
            dealerHiddenCard.setImageResource(
                    getResources().getIdentifier(
                            dealerHand.getCards().get(1).getImageFileName(),
                            "drawable",
                            getPackageName()
                    )
            );
            updateDealerScore();
        }

        // Determine winner
        if (dealerScore > 21 || (playerScore <= 21 && playerScore > dealerScore)) {
            endGame("Player wins!");
        } else if (playerScore == dealerScore) {
            endGame("It's a tie!");
        } else {
            endGame("Dealer wins!");
        }
    }

    private void dealCard(Hand hand, ImageView imageView) {
        Card card = drawCard();
        hand.addCard(card);
        if (imageView != null) {
            imageView.setImageResource(
                    getResources().getIdentifier(
                            card.getImageFileName(),
                            "drawable",
                            getPackageName()
                    )
            );
        }
    }

    private void dealHiddenCard(Hand hand, ImageView imageView) {
        Card card = drawCard();
        hand.addCard(card);
        imageView.setImageResource(R.drawable.card_back); // Set hidden card image
    }

    private void endGame(String message) {
        TextView winnerText = findViewById(R.id.winner);
        winnerText.setText(message);
    }

    private void updatePlayerScore() {
        TextView playerScoreTextView = findViewById(R.id.playerScore);
        playerScoreTextView.setText("" + playerScore);
    }

    private void updateDealerScore() {
        TextView dealerScoreTextView = findViewById(R.id.dealerScore);
        dealerScoreTextView.setText("" + dealerScore);
    }

    private void initializeDeck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
    }

    private Card drawCard() {
        Random random = new Random();
        int index = random.nextInt(deck.size());
        return deck.remove(index);
    }
}
