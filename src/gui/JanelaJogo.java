package gui;

import main.Game;
import javax.swing.JFrame;

public class JanelaJogo extends JFrame {
    public JanelaJogo(Game game) {
        setTitle("Forever Lasting Edible Slaughter Harvest (FLESH)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PainelJogo painel = new PainelJogo(game);
        add(painel);

        game.setPainelJogo(painel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
