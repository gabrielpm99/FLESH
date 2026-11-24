package main;

import gui.PainelJogo;
import input.InputTeclado;
import interfaces.Desenhavel;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import main.jogador.Jogador;
import main.mapa.Mapa;
import main.monstros.Monstro;

public class Game {
    public static final int LARGURA_JANELA = 1366;
    public static final int ALTURA_JANELA = 768;

    public static final int FPS_ALVO = 60;
    private static final double NANOS_POR_TICK = 1_000_000_000.0 / FPS_ALVO;

    public static final int LARGURA_MAPA = 600;
    public static final int ALTURA_MAPA = 600;

    public static final int LARGURA_JOGADOR = 15;
    public static final int ALTURA_JOGADOR = 15;
    public static final int VELOCIDADE_JOGADOR = 4;

    public static final int LARGURA_MONSTRO = 15;
    public static final int ALTURA_MONSTRO = 15;
    public static final int VELOCIDADE_MONSTRO = 1;
    public static final int DISTANCIA_DE_FUGA = 75;
    public static final int QUANTIDADE_MONSTROS = 5;
    public static final int TEMPO_RESPAWN_MONSTROS_MS = 3000;
    private static final long TEMPO_RESPAWN_NANOS = TEMPO_RESPAWN_MONSTROS_MS * 1_000_000L;

    private volatile double px_por_tick;
    private volatile boolean rodando = false;
    private Thread thread_jogo;
    private long acumulador_respawn_nanos = 0;

    private PainelJogo painel;

    private final Random randomico = new Random();
    
    private final Mapa mapa;
    private final Jogador jogador;

    private final InputTeclado input_teclado;

    private final List<Monstro> lista_monstros;
    private final List<Desenhavel> lista_elementos_desenhaveis;


    public Game() {
        this.mapa = new Mapa(LARGURA_MAPA, ALTURA_MAPA, LARGURA_JANELA, ALTURA_JANELA);

        int x_inicial_jogador = (mapa.getCoordX() + ((mapa.getLargura() - LARGURA_JOGADOR) / 2));
        int y_inicial_jogador = (mapa.getCoordY() + ((mapa.getAltura() - ALTURA_JOGADOR) / 2));

        this.jogador = new Jogador(LARGURA_JOGADOR, ALTURA_JOGADOR, VELOCIDADE_JOGADOR, x_inicial_jogador, y_inicial_jogador);

        this.lista_elementos_desenhaveis = new ArrayList<>();
        this.popularListaDesenhaveis();

        this.input_teclado = new InputTeclado(jogador);

        this.lista_monstros = new ArrayList<>();
        this.popularListaMonstros();
    }


    private void executarLoop() {
        long ultimoTempo = System.nanoTime();
        double delta = 0;

        while (this.rodando) {
            long agora = System.nanoTime();
            long decorrido = agora - ultimoTempo;
            ultimoTempo = agora;

            delta += decorrido / NANOS_POR_TICK;

            boolean atualizou = false;
            while (delta >= 1.0) {
                atualizar();
                delta -= 1.0;
                atualizou = true;
            }

            if (atualizou) {
                atualizarPainel();
            }

            long tempoRestanteMs = (long) ((NANOS_POR_TICK - (System.nanoTime() - ultimoTempo)) / 1_000_000L);
            if (tempoRestanteMs > 1) {
                try {
                    Thread.sleep(tempoRestanteMs - 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void atualizar() {
        int x_antes = jogador.getCoordX();
        int y_antes = jogador.getCoordY();

        jogador.movimentar(mapa);

        int dx = jogador.getCoordX() - x_antes;
        int dy = jogador.getCoordY() - y_antes;

        px_por_tick = Math.sqrt(dx * dx + dy * dy);

        gerenciarMonstros();
        gerenciarRespawn();
    }

    private void gerenciarRespawn() {
        if (this.lista_monstros.size() < QUANTIDADE_MONSTROS) {
            this.acumulador_respawn_nanos += (long) NANOS_POR_TICK;

            if (this.acumulador_respawn_nanos >= TEMPO_RESPAWN_NANOS) {
                gerarMonstros();
                this.acumulador_respawn_nanos = 0;
            }
        } else {
            this.acumulador_respawn_nanos = 0;
        }
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
                this.jogador.matarMonstro();

                iterador_monstros.remove();
                synchronized (this.lista_elementos_desenhaveis) {
                    this.lista_elementos_desenhaveis.remove(monstro);
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
        synchronized (this.lista_elementos_desenhaveis) {
            this.lista_elementos_desenhaveis.addFirst(monstro);
        }
    }

    private void popularListaMonstros() {
        while(this.lista_monstros.size() < QUANTIDADE_MONSTROS) {
            gerarMonstros();
        }
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
        if (this.rodando) {
            return;
        }

        this.rodando = true;
        this.thread_jogo = new Thread(this::executarLoop, "GameLoop");
        this.thread_jogo.start();
    }

    public void parar() {
        this.rodando = false;
    }


    private void popularListaDesenhaveis() {
        this.lista_elementos_desenhaveis.add(mapa);
        this.lista_elementos_desenhaveis.add(jogador);
    }

    public void desenharElementos(Graphics2D g2d) {
        if(g2d == null) {
            throw new IllegalArgumentException("A instância de g2d (gui) não pode ser nula");
        }

        synchronized (this.lista_elementos_desenhaveis) {
            for(Desenhavel elemento : this.lista_elementos_desenhaveis) {
                elemento.desenhar(g2d);
            }
        }
    }

    public double getPxPortick() {
        return this.px_por_tick;
    }
}