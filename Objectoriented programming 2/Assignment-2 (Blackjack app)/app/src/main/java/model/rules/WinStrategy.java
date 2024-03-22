package model.rules;

import model.Player;

/**
 * Rule interface that encapsulates who wins the game.
 */
public interface WinStrategy extends AcceptVisitor {

  // TODO dont store max score at 2 places
  final int maxScore = 21;

  /**
   * Checks who won the game.

   * @param dealer the dealer who is playing
   * @param player the player who is playing
   * @return True if the rule decided that dealer is winner.
   */
  boolean isDealerWinner(Player dealer, Player player);
}
