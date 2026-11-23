package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import input.InputTeclado;
import main.Game;

public class PainelJogo extends JPanel {
    private final Game game;

    private final InputTeclado input_teclado;

    public PainelJogo(Game game) {
        if(game == null) {
            throw new IllegalArgumentException("A instância de game não pode ser nula");
        }


        this.game = game;

        this.input_teclado = game.getInputTeclado();
        
        addKeyListener(input_teclado);
        setFocusable(true);

        setPreferredSize(new Dimension(Game.LARGURA_JANELA, Game.ALTURA_JANELA));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, Game.LARGURA_JANELA, Game.ALTURA_JANELA);

        game.desenharElementos(g2d);
    }
}
