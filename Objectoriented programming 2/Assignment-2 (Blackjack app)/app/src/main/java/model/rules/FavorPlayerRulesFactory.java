package model.rules;

/**
 * Creates concrete rules.
 */
public class FavorPlayerRulesFactory implements AbstractRulesFactory {

  /**
   * Creates the rule to use for the dealer's hit behavior.

   * @return The rule to use
   */
  public HitStrategy getHitRule() {
    return new BasicHitStrategy();
  }

  /**
   * Crates the rule to use when starting a new game.

   * @return The rule to use.
   */
  public NewGameStrategy getNewGameRule() {
    return new InternationalNewGameStrategy();
  }


  /**
   * Crates the rule to use when deciding who wins.

   * @return The rule to use.
   */
  public WinStrategy getNewWinRule() {
    return new PlayerWinEqual();
  }
}