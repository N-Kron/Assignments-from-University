package model;

/**
 * observer interface.
 */
public interface Observer {
  void update(Iterable<model.Card> hand, int score, boolean isDealer);

}
