package gui;

import config.Argumentos;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import javax.swing.JPanel;
import main.Game;

public class PainelJogo extends JPanel {
    private final Game game;

    public PainelJogo(Game game) {
        if(game == null) {
            throw new IllegalArgumentException("A instância de game não pode ser nula");
        }


        this.game = game;
        
        addKeyListener(game.getInputTeclado());
        setFocusable(true);

        setPreferredSize(new Dimension(Game.LARGURA_JANELA, Game.ALTURA_JANELA));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, Game.LARGURA_JANELA, Game.ALTURA_JANELA);
        
        
        if (Arrays.asList(Argumentos.obter()).contains("--debug")) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 32));
            g2d.drawString(String.format("SPEED = %.2f", game.getPxPortick()), 50, 50);
        }

        game.desenharElementos(g2d);
    }
}
