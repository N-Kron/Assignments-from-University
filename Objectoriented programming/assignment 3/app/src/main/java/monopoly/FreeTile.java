package monopoly;

/**
 * Represents a free tile that does nothing.
 */
public class FreeTile extends Tile {

  public FreeTile(Tile prevTile) {
    super(prevTile);
  }

  @Override
  public void visit(Player player) {

  }

  @Override
  public boolean buy(Player player) {
    return false;
  }

  @Override
  public String toString() {
    return "Free";
  }
}
