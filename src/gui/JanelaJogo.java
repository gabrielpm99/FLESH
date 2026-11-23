package gui;

import main.Game;
import javax.swing.JFrame;

public class JanelaJogo extends JFrame {
    public JanelaJogo(Game game) {
        if(game == null) {
            throw new IllegalArgumentException("A instância de game não pode ser nula");
        }


        setTitle("Forever Lasting Edible Slaughter Harvest (FLESH)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PainelJogo painel = new PainelJogo(game);
        add(painel);

        game.setPainel(painel);

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        painel.requestFocusInWindow();
    }
}
