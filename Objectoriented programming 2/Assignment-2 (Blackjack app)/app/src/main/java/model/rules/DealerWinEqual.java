package model.rules;

import model.Player;

/**
 * This class implements the WinStrategy interface and defines the rules for determining if the dealer is the winner.
 */
public class DealerWinEqual implements WinStrategy {

  /**
   * Determines if the dealer is the winner based on the scores of the dealer and player.
   *
   * @param dealer The dealer player.
   * @param player The non-dealer player.
   * @return true if the dealer is considered the winner, false otherwise.
   *         The dealer wins under the following conditions:
   *         1. The player's score exceeds the maximum score.
   *         2. The dealer's score does not exceed the maximum score and is greater than or equal to the player's score.
   */
  public boolean isDealerWinner(Player dealer, Player player) {
    if (player.calcScore() > maxScore) {
      return true;
    } else if (dealer.calcScore() > maxScore) {
      return false;
    }
    return dealer.calcScore() >= player.calcScore();
  }

  @Override
  public void accept(RuleVisitor visitor) {
    visitor.visitDealerWinEqual();
  }
}
