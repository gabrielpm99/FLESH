import config.Argumentos;
import gui.JanelaJogo;
import javax.swing.SwingUtilities;
import main.Game;

public class Main {
    public static void main(String[] args) {
        Argumentos.definir(args);

        Game game = new Game();

        SwingUtilities.invokeLater(() -> {
            new JanelaJogo(game);

            game.iniciar();
        });
    }
}