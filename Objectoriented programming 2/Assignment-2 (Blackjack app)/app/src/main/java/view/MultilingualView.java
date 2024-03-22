package view;

import java.util.ResourceBundle;

/**
 * Implements an english console view.
 */
public class MultilingualView implements View {

  private final ResourceBundle rb;


  public MultilingualView(ResourceBundle bundle) {
    rb = ResourceBundle.getBundle(bundle.getBaseBundleName(), bundle.getLocale());
  }

  /** this is the display and read input for player actions.
   *
   * @return action
   */
  public PlayerActions showPlayerActions() {
    PlayerActions action = PlayerActions.None;
    while (action == PlayerActions.None) {
      action = toPlayerActions();
    }
    return action;
  }

  /** this is the display menu handler. */
  private PlayerActions toPlayerActions() {
    switch (getInput()) {
      case 'p':
        return PlayerActions.Play;
      case 'h':
        return PlayerActions.Hit;
      case 's':
        return PlayerActions.Stand;
      case 'q':
        return PlayerActions.Quit; 
      default:
        return PlayerActions.None; 
    }
  }

  /**
   * Shows a welcome message.
   */
  public void displayWelcomeMessage() {
    for (int i = 0; i < 50; i++) {
      System.out.print("\n");
    }
    System.out.println(rb.getString("greeting"));
    System.out.println(rb.getString("guide") + "\n");
  }

  /**
   * Returns pressed characters from the keyboard.

   * @return the pressed character.
   */
  public int getInput() {
    try {
      int c = System.in.read();
      while (c == '\r' || c == '\n') {
        c = System.in.read();
      }
      return c;
    } catch (java.io.IOException e) {
      System.out.println("" + e);
      return 0;
    }
  }

  /**
   * Displays a card.

   * @param card The card to display.
   */
  public void displayCard(model.Card card) {
    if (card.getColor() == model.Card.Color.Hidden) {
      System.out.println(rb.getString("displayCardHidden"));
    } else {
      String[] colors = rb.getString("cardColorArray").split(", ");
      String[] values = rb.getString("cardValueArray").split(", ");
      System.out.println("" + values[card.getValue().ordinal()] + " "
          + rb.getString("displayCardfiller") + " " + colors[card.getColor().ordinal()]);
    }
  }

  public void displayPlayerHand(Iterable<model.Card> hand, int score) {
    displayHand(rb.getString("displayHandTypePlayer"), hand, score);
  }

  public void displayDealerHand(Iterable<model.Card> hand, int score) {
    displayHand(rb.getString("displayHandTypeDealer"), hand, score);
  }

  private void displayHand(String name, Iterable<model.Card> hand, int score) {
    System.out.println(name + " " + rb.getString("displayHandHas") + " ");
    for (model.Card c : hand) {
      displayCard(c);
    }
    System.out.println(rb.getString("displayHandScore") + " " + score);
    System.out.println("");
  }

  /**
   * Displays the winner of the game.

   * @param dealerIsWinner True if the dealer is the winner.
   */
  public void displayGameOver(boolean dealerIsWinner) {
    System.out.println(rb.getString("displayGameOverEnd") + " ");
    if (dealerIsWinner) {
      System.out.println(rb.getString("displayGameOverDealer"));
    } else {
      System.out.println(rb.getString("displayGameOverYou"));
    }

  }

  @Override
  public void update(Iterable<model.Card> hand, int score, boolean isDealer) {
    if (isDealer) {
      displayDealerHand(hand, score);
    } else {
      displayPlayerHand(hand, score);
    }
    try {
      Thread.sleep(2000); // Pause for 2 seconds
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void printRules() {

  }

  @Override
  public void visitSoftHitStrategy() {
    System.out.println(rb.getString("soft17Hit"));
  }

  @Override
  public void visitBasicHitStrategy() {
    System.out.println(rb.getString("basicHit"));
  }

  @Override
  public void visitInternationalNewGameStrategy() {
    System.out.println(rb.getString("InternationalNewGame"));
  }

  @Override
  public void visitAmericanNewGameStrategy() {
    System.out.println(rb.getString("AmericanNewGame"));
  }

  @Override
  public void visitDealerWinEqual() {
    System.out.println(rb.getString("DealerWinEqual"));
  }

  @Override
  public void visitPlayerWinEqual() {
    System.out.println(rb.getString("PlayerWinEqual"));
  }

}
