package model.rules;

import model.Card;
import model.Card.Value;
import model.Player;

class SoftHitStrategy implements HitStrategy {
  private static final int hitLimit = 17;

  public boolean doHit(Player dealer) {
    boolean softSeventeen = hasAce(dealer.getHand());
    boolean belowLimit = dealer.calcScore() < hitLimit;
    boolean atLimit = dealer.calcScore() == hitLimit;
    if (belowLimit || (softSeventeen && atLimit)) {
      return true;
    }
    return false;
  }


  private boolean hasAce(Iterable<Card> hand) {
    for (Card c : hand) {
      if (c.getValue() == Value.Ace) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void accept(RuleVisitor visitor) {
    visitor.visitSoftHitStrategy();
  }
}
