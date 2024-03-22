package monopoly;

/**
 * Represents a player and offers human interaction via ConsoleUi.
 */
public class ComputerPlayer extends HumanPlayer {
  private ConsoleUi ui = null;

  /**
   * Creates a new Player.
   *
   * @param startTile The tile the player should start on.
   * @param name      The name of the player.
   * @param ui        The user interface that the player uses to send messages.
   */
  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "The tile objects are shared between all player and the board.")
  public ComputerPlayer(Tile startTile, String name, ConsoleUi ui) {
    super(startTile, name, ui);
    this.ui = ui;
  }

  /**
   * Handles the interaction when it is the players turn to perform some actions.
   */
  public boolean yourTurn(Dice d1, Dice d2) {
    if (Game.getRound() < 7) {
      buyCurrentTile();
    }
    d1.roll();
    d2.roll();
    final int steps1 = d1.getValue();
    final int steps2 = d2.getValue();
    ui.playerMoves(getName(), steps1, steps2);
    move(steps1 + steps2);
    return true;
  }
}