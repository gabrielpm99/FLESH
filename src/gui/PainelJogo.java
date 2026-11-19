package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import input.InputTeclado;
import main.Game;

public class PainelJogo extends JPanel {
    public static final int LARGURA_JANELA = 1366;
    public static final int ALTURA_JANELA = 768;

    private Game game;

    private InputTeclado input_teclado;

    public PainelJogo(Game game) {
        this.game = game;

        this.input_teclado = new InputTeclado(game.jogador);
        
        addKeyListener(input_teclado);
        setFocusable(true);
        requestFocusInWindow();

        setPreferredSize(new Dimension(LARGURA_JANELA, ALTURA_JANELA));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, LARGURA_JANELA, ALTURA_JANELA);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(game.mapa.getCoordXMapa(), 
                     game.mapa.getCoordYMapa(), 
                     game.mapa.getLarguraMapa(), 
                     game.mapa.getAlturaMapa());

        g2d.setColor(Color.BLUE);
        g2d.fillRect(game.jogador.getCoordXJogador(), 
                     game.jogador.getCoordYJogador(), 
                     game.jogador.getLarguraJogador(), 
                     game.jogador.getAlturaJogador());
    }
}
