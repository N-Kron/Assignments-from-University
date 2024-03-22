package model;

import model.rules.AbstractRulesFactory;
import model.rules.AcceptVisitor;
import model.rules.HitStrategy;
import model.rules.NewGameStrategy;
import model.rules.RuleVisitor;
import model.rules.WinStrategy;

/**
 * Represents a dealer player that handles the deck of cards and runs the game
 * using rules.
 */
public class Dealer extends Player implements AcceptVisitor {

  private Deck deck;
  private NewGameStrategy newGameRule;
  private HitStrategy hitRule;
  private WinStrategy winRule;

  /**
   * Initializing constructor.
   *
   * @param rulesFactory A factory that creates the rules to use.
   */
  public Dealer(AbstractRulesFactory rulesFactory) {

    newGameRule = rulesFactory.getNewGameRule();
    hitRule = rulesFactory.getHitRule();
    winRule = rulesFactory.getNewWinRule();
  }

  private void dealCardToPlayer(Player player) {
    if (deck != null && !isGameOver()) {
      Card.Mutable c = deck.getCard();
      c.show(true);
      player.dealCard(c);
    }
  }

  /**
   * Starts a new game if the game is not currently under way.
   *
   * @param player The player to play agains.
   * @return True if the game could be started.
   */
  public boolean newGame(Player player) {
    if (deck == null || isGameOver()) {
      deck = new Deck();
      clearHand();
      player.clearHand();
      return newGameRule.newGame(deck, this, player);
    }
    return false;
  }

  /**
   * Gives the player one more card if possible. I.e. the player hits.
   *
   * @param player The player to give a card to.
   * @return true if the player could get a new card, false otherwise.
   */
  public boolean hit(Player player) {
    if (player.calcScore() < maxScore) {
      dealCardToPlayer(player);
      return true;
    }
    return false;
  }

  /**
   * Checks if the dealer is the winner compared to a player.
   *
   * @param player The player to check agains.
   * @return True if the dealer is the winner, false if the player is the winner.
   */
  public boolean isDealerWinner(Player player) {
    return winRule.isDealerWinner(this, player);
  }

  /**
   * Checks if the game is over, i.e. the dealer can take no more cards.
   *
   * @return True if the game is over.
   */
  public boolean isGameOver() {
    if (deck != null && hitRule.doHit(this) != true) {
      return true;
    }
    return false;
  }

  /**
   * The player has choosen to take no more cards, it is the dealers turn.
   */
  public boolean stand() {
    if (deck != null) {
      showHand();
      while (hitRule.doHit(this)) {
        dealCardToPlayer(this);
      }
      return true;
    }
    return false;
  }

  @Override
  public void accept(RuleVisitor visitor) {
    newGameRule.accept(visitor);
    hitRule.accept(visitor);
    winRule.accept(visitor);
  }

}