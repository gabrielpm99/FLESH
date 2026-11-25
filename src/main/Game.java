package main;

import gui.PainelJogo;
import input.InputTeclado;
import interfaces.Desenhavel;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import main.jogador.Jogador;
import main.mapa.Mapa;
import main.monstros.Monstro;

public class Game {
    public static final int LARGURA_JANELA = 1366;
    public static final int ALTURA_JANELA = 768;
    
    public static final int INTERVALO_FRAMES_MS = 16;

    public static final int LARGURA_MAPA = 600;
    public static final int ALTURA_MAPA = 600;

    public static final int LARGURA_JOGADOR = 15;
    public static final int ALTURA_JOGADOR = 15;
    public static final int VELOCIDADE_JOGADOR = 4;

    public static final int LARGURA_MONSTRO = 10;
    public static final int ALTURA_MONSTRO = 10;
    public static final int VELOCIDADE_MONSTRO = 3;
    public static final int DISTANCIA_DE_FUGA = 75;
    public static final int QUANTIDADE_MONSTROS = 10;
    public static final int TEMPO_RESPAWN_MONSTROS_MS = 3000;

    private double px_por_tick;


    private final Timer timer_jogo;
    private final Timer timer_respawn_monstros;

    private PainelJogo painel;

    private final Random randomico = new Random();
    
    private final Mapa mapa;
    private final Jogador jogador;
    private final InputTeclado input_teclado;

    private final List<Monstro> lista_monstros;
    private final List<Desenhavel> lista_elementos_desenhaveis;

    private int qtd_monstros_mortos;


    public Game() {
        this.mapa = new Mapa(LARGURA_MAPA, ALTURA_MAPA, LARGURA_JANELA, ALTURA_JANELA);

        int x_inicial_jogador = (mapa.getCoordX() + ((mapa.getLargura() - LARGURA_JOGADOR) / 2));
        int y_inicial_jogador = (mapa.getCoordY() + ((mapa.getAltura() - ALTURA_JOGADOR) / 2));

        this.jogador = new Jogador(LARGURA_JOGADOR, ALTURA_JOGADOR, VELOCIDADE_JOGADOR, x_inicial_jogador, y_inicial_jogador);

        this.lista_elementos_desenhaveis = new ArrayList<>();
        this.popularListaDesenhaveis();

        this.input_teclado = new InputTeclado(jogador);



        this.timer_respawn_monstros = new Timer(TEMPO_RESPAWN_MONSTROS_MS, new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                gerarMonstros();
            }
        });

        this.lista_monstros = new ArrayList<>();
        this.popularListaMonstros();



        this.timer_jogo = new Timer(INTERVALO_FRAMES_MS, new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                int x_antes = jogador.getCoordX();
                int y_antes = jogador.getCoordY();

                jogador.movimentar(mapa);

                int dx = jogador.getCoordX() - x_antes;
                int dy = jogador.getCoordY() - y_antes;

                px_por_tick = Math.sqrt(dx * dx + dy * dy);

                gerenciarMonstros();

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


    private void gerenciarMonstros() {
        Iterator<Monstro> iterador_monstros = this.lista_monstros.iterator();

        while(iterador_monstros.hasNext()) {
            Monstro monstro = iterador_monstros.next();

            monstro.fugir(jogador);
            monstro.movimentar(mapa);

            if(this.jogador.checarColisao(monstro)) {
                matarMonstro();

                iterador_monstros.remove();
                this.lista_elementos_desenhaveis.remove(monstro);

                if(!this.timer_respawn_monstros.isRunning()) {
                    this.timer_respawn_monstros.start();
                }
            }
        }
    }

    private void gerarMonstros() {
        Monstro monstro;
            
        do {
            int coord_x_aleatoria = (this.mapa.getCoordX() + this.randomico.nextInt(this.mapa.getLargura() - LARGURA_MONSTRO));
            int coord_y_aleatoria = (this.mapa.getCoordY() + this.randomico.nextInt(this.mapa.getAltura() - ALTURA_MONSTRO));

            monstro = new Monstro(LARGURA_MONSTRO,
                ALTURA_MONSTRO,
                VELOCIDADE_MONSTRO,
                coord_x_aleatoria,
                coord_y_aleatoria,
                DISTANCIA_DE_FUGA
            );
        } while(this.jogador.checarColisao(monstro));

        this.lista_monstros.add(monstro);
        this.lista_elementos_desenhaveis.addFirst(monstro);

        if(this.lista_monstros.size() >= QUANTIDADE_MONSTROS) {
            this.timer_respawn_monstros.stop();
        }
    }

    private void popularListaMonstros() {
        while(this.lista_monstros.size() < QUANTIDADE_MONSTROS) {
            gerarMonstros();
        }
    }

    private void matarMonstro() {
        this.qtd_monstros_mortos++;
    }


    public Mapa getMapa() {
        return this.mapa;
    }

    public Jogador getJogador() {
        return this.jogador;
    }

    public InputTeclado getInputTeclado() {
        return this.input_teclado;
    }


    public void setPainel(PainelJogo painel) {
        if(painel == null) {
            throw new IllegalArgumentException("A instância de painel não pode ser nula");
        }

        this.painel = painel;
    }

    public void iniciar() {
        this.timer_jogo.start();
    }

    public void parar() {
        this.timer_jogo.stop();
        this.timer_respawn_monstros.stop();
    }


    private void popularListaDesenhaveis() {
        this.lista_elementos_desenhaveis.add(mapa);
        this.lista_elementos_desenhaveis.add(jogador);
    }

    public void desenharElementos(Graphics2D g2d) {
        if(g2d == null) {
            throw new IllegalArgumentException("A instância de g2d (gui) não pode ser nula");
        }

        for(Desenhavel elemento : this.lista_elementos_desenhaveis) {
            elemento.desenhar(g2d);
        }
    }

    public double getPxPortick() {
        return this.px_por_tick;
    }
}
