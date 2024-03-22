package controller;

import java.util.Locale;
import java.util.ResourceBundle;
import model.Game;
import view.MultilingualView;
import view.View;

/**
 * Starts the application using the console.
 */
public class App {
  /**
   * Starts the game.

  * @param args Not used.
  */
  public static void main(String[] args) {
    ResourceBundle bundle = ResourceBundle.getBundle("lang/AppMessages", new Locale("sv", "SE"));
    View v = new MultilingualView(bundle);
    Game g = new Game(v, v, new model.rules.FavorDealerRulesFactory());
    Player ctrl = new Player();

    while (ctrl.play(g, v)) {

    }
  }
}