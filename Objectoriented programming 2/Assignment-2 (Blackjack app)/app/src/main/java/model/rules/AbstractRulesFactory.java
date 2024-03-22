package model.rules;

/**
 * Abstract rules factory for the black jack game.
 */
public interface AbstractRulesFactory {
  /**
   * Creates the rule to use for the dealer's hit behavior.

   * @return The rule to use
   */
  public HitStrategy getHitRule();

  /**
   * Crates the rule to use when starting a new game.

   * @return The rule to use.
   */
  public NewGameStrategy getNewGameRule();


  /**
   * Crates the rule to use when deciding who wins.

   * @return The rule to use.
   */
  public WinStrategy getNewWinRule();
}
