package model.rules;

/**
 * visitor that visits rules.
 */
public interface RuleVisitor {
  public void visitSoftHitStrategy();

  public void visitBasicHitStrategy();

  public void visitInternationalNewGameStrategy();

  public void visitAmericanNewGameStrategy();

  public void visitDealerWinEqual();

  public void visitPlayerWinEqual();
}
