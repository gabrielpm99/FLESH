package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import main.mapa.Mapa;
import main.jogador.Jogador;
import gui.PainelJogo;

public class Game {
    private final Timer timer_jogo;
    private PainelJogo painel;
    
    public final Mapa mapa = new Mapa(600, 600);
    public final Jogador jogador = new Jogador(15, 15, 5);

    public Game() {
        jogador.setPosicaoInicialJogador(((mapa.getCoordXMapa() + mapa.getLarguraMapa()) / 2), 
                                         ((mapa.getCoordYMapa() + mapa.getAlturaMapa()) / 2));

        this.timer_jogo = new Timer(16, new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                jogador.movimentarJogador(mapa);

                atualizarPainel();
            }
        });
    }


    private void atualizarPainel() {
        if(painel == null) {
            return;
        }

        painel.repaint();
    }


    public void setPainelJogo(PainelJogo painel) {
        this.painel = painel;
    }

    public void iniciarJogo() {
        this.timer_jogo.start();
    }

    public void pararJogo() {
        this.timer_jogo.stop();
    }
}
