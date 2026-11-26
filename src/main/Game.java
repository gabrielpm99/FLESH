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
    private static final long NANOS_POR_MS = 1_000_000L;
    private static final double NANOS_POR_TICK = 1_000_000_000.0 / FPS_ALVO;

    public static final int LARGURA_MAPA = 600;
    public static final int ALTURA_MAPA = 600;

    public static final int LARGURA_JOGADOR = 16;
    public static final int ALTURA_JOGADOR = 16;
    public static final int VELOCIDADE_JOGADOR = 4;

    public static final int LARGURA_MONSTRO = 16;
    public static final int ALTURA_MONSTRO = 16;
    public static final int VELOCIDADE_MONSTRO = 1;
    public static final int DISTANCIA_DE_FUGA = 75;
    public static final int QUANTIDADE_MONSTROS = 5;
    public static final int TEMPO_RESPAWN_MONSTROS_MS = 3000;
    private static final long TEMPO_RESPAWN_NANOS = TEMPO_RESPAWN_MONSTROS_MS * NANOS_POR_MS;

    private volatile double px_por_tick;
    private volatile boolean jogo_rodando = false;
    private Thread thread_jogo;
    private long tempo_respawn_decorrido_nanos = 0;

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

        this.lista_monstros = new ArrayList<>();
        this.popularListaMonstros();
    }


    private void executarLoop() {
        long ultimo_tempo_nanos = System.nanoTime();
        double ticks_decorridos = 0;

        while (this.jogo_rodando) {
            long tempo_atual_nanos = System.nanoTime();
            long tempo_decorrido_nanos = tempo_atual_nanos - ultimo_tempo_nanos;
            ultimo_tempo_nanos = tempo_atual_nanos;

            ticks_decorridos += tempo_decorrido_nanos / NANOS_POR_TICK;

            boolean houve_atualizacao = false;
            while (ticks_decorridos >= 1.0) {
                atualizarEstadosJogo();
                ticks_decorridos -= 1.0;
                houve_atualizacao = true;
            }

            if (houve_atualizacao) {
                atualizarPainel();
            }

            long tempo_restante_ms = (long) ((NANOS_POR_TICK - (System.nanoTime() - ultimo_tempo_nanos)) / NANOS_POR_MS);
            if (tempo_restante_ms > 1) {
                try {
                    Thread.sleep(tempo_restante_ms - 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void atualizarEstadosJogo() {
        int x_antes_movimento = jogador.getCoordX();
        int y_antes_movimento = jogador.getCoordY();

        jogador.movimentar(mapa);

        int variacao_coord_x = jogador.getCoordX() - x_antes_movimento;
        int variacao_coord_y = jogador.getCoordY() - y_antes_movimento;

        px_por_tick = Math.sqrt((variacao_coord_x * variacao_coord_x) + (variacao_coord_y * variacao_coord_y));

        gerenciarMonstros();
        gerenciarRespawn();
    }

    private void gerenciarRespawn() {
        if (this.lista_monstros.size() < QUANTIDADE_MONSTROS) {
            this.tempo_respawn_decorrido_nanos += (long) NANOS_POR_TICK;

            if (this.tempo_respawn_decorrido_nanos >= TEMPO_RESPAWN_NANOS) {
                gerarMonstros();
                this.tempo_respawn_decorrido_nanos = 0;
            }
        } else {
            this.tempo_respawn_decorrido_nanos = 0;
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
                matarMonstro();

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
        if (this.jogo_rodando) {
            return;
        }

        this.jogo_rodando = true;
        this.thread_jogo = new Thread(this::executarLoop, "GameLoop");
        this.thread_jogo.start();
    }

    public void parar() {
        this.jogo_rodando = false;
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