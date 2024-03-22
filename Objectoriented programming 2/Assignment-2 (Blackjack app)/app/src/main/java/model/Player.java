package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a player in the Black Jack game. A Player has a hand of cards.
 */
public class Player {

  private List<Card.Mutable> hand;
  private List<Observer> subscribers = new ArrayList<>();
  protected final int maxScore = 21;

  public Player() {
    hand = new LinkedList<Card.Mutable>();
  }

  public void addSubscriber(Observer observer) {
    subscribers.add(observer);
  }

  /**
 * Notifies all subscribers about the current state of the player.
 * Each subscriber is updated with the player's hand, score, and whether the player is a dealer.
 */
  public void notifySubscribers() {
    for (Observer subscriber : subscribers) {
      subscriber.update(getHand(), calcScore(), this instanceof Dealer);
    }
  }

  /**
   * Adds a card to the Player's hand. 

   * @param addToHand The card to add to the hand.
   */
  public void dealCard(Card.Mutable addToHand) {
    hand.add(addToHand);
    notifySubscribers();
  }

  /**
   * Returns the cards in thand.

   * @return the cards in the Player's hand
   */
  public Iterable<Card> getHand() {
    return new LinkedList<Card>(hand);
  }

  /**
   * Removes all cards from the hand.
   */
  public void clearHand() {
    hand.clear();
  }

  /**
   * Shows all cards in the hand.
   */
  public void showHand() {
    for (Card.Mutable c : hand) {
      c.show(true);
    }
  }

  /**
   * Calculates the score of the hand according to Black Jack rules.

   * @return The score.
   */
  public int calcScore() {
    // the number of scores is dependant on the number of scorable values
    // as it seems there is no way to do this check at compile time in java ?!
    // cardScores[13] = {...};
    int[] cardScores = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11 };
    assert (cardScores.length == Card.Value.Count.ordinal())
        : "Card Scores array size does not match number of card values";

    int score = 0;

    for (Card c : getHand()) {
      if (c.getValue() != Card.Value.Hidden) {
        score += cardScores[c.getValue().ordinal()];
      }
    }

    if (score > maxScore) {
      for (Card c : getHand()) {
        if (c.getValue() == Card.Value.Ace && score > maxScore) {
          score -= 10;
        }
      }
    }

    return score;
  }
}
