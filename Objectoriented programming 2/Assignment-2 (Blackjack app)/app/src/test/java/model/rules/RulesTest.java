package model.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;

import model.Dealer;
import model.Game;
import model.Card.Mutable;
import model.rules.*;
import model.rules.HitStrategy;
import model.rules.SoftHitStrategy;
import org.junit.jupiter.api.Test;

/**
 * test for soft17 strategy.
 */
public class RulesTest {

  @Test public void softSeventeenTest() {
    AbstractRulesFactory factory = new FavorDealerRulesFactory();
    boolean isSoftHitStrategy = factory.getHitRule().getClass() == new SoftHitStrategy().getClass();
    if (isSoftHitStrategy) {
      softOne(factory);
      softTwo(factory);
      softThree(factory);
    }
  }

  // test whether dealer hits when having soft 17
  void softOne(AbstractRulesFactory factory) {
    Dealer testDealer = new Dealer(factory);
    Mutable testAce = new Mutable(model.Card.Color.Clubs, model.Card.Value.Ace);
    Mutable testSix = new Mutable(model.Card.Color.Clubs, model.Card.Value.Six);
    testDealer.dealCard(testAce);
    testDealer.dealCard(testSix);
    testDealer.showHand();
    HitStrategy strategy = factory.getHitRule();
    assertTrue(strategy.doHit(testDealer));
  }

  // test whether dealer hits when having soft 18
  void softTwo(AbstractRulesFactory factory) {
    Dealer testDealer = new Dealer(factory);
    Mutable testAce = new Mutable(model.Card.Color.Clubs, model.Card.Value.Ace);
    Mutable testSix = new Mutable(model.Card.Color.Clubs, model.Card.Value.Seven);
    testDealer.dealCard(testAce);
    testDealer.dealCard(testSix);
    testDealer.showHand();
    HitStrategy strategy = factory.getHitRule();
    assertTrue(!(strategy.doHit(testDealer)));
  }

  // test whether dealer hits when having 16
  void softThree(AbstractRulesFactory factory) {
    Dealer testDealer = new Dealer(factory);
    Mutable testAce = new Mutable(model.Card.Color.Clubs, model.Card.Value.Ten);
    Mutable testSix = new Mutable(model.Card.Color.Clubs, model.Card.Value.Six);
    testDealer.dealCard(testAce);
    testDealer.dealCard(testSix);
    testDealer.showHand();
    HitStrategy strategy = factory.getHitRule();
    assertTrue(strategy.doHit(testDealer));
  }

}
