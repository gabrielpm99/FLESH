import gui.JanelaJogo;
import main.Game;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();

        SwingUtilities.invokeLater(() -> {
            new JanelaJogo(game);

            game.iniciar();
        });
    }
}