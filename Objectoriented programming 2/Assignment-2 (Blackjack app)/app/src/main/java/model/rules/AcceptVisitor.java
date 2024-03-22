package model.rules;

/**
 * interface implemented by classes that want to accept a visitor.
 */
public interface AcceptVisitor {
  public void accept(RuleVisitor visitor);
}
