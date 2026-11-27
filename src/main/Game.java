package main;

import gui.PainelJogo;
import input.InputTeclado;
import interfaces.Desenhavel;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.jogador.Jogador;
import main.jogador.JogadorBasico;
import main.mapa.Mapa;
import main.mapa.MapaBasico;
import main.monstros.Monstro;
import main.monstros.MonstroBasico;
import main.regras.Consumo;
import main.regras.Fome;
import main.serras.Serra;
import main.serras.SerraPrimeira;

public class Game {
    public static final int LARGURA_JANELA = 1366;
    public static final int ALTURA_JANELA = 768;

    private static final int FPS_ALVO = 60;
    private static final long NANOS_POR_MS = 1_000_000L;
    private static final double NANOS_POR_TICK = 1_000_000_000.0 / FPS_ALVO;

    public static final int QUANTIDADE_MONSTROS = 5;

    private volatile double px_por_tick;
    private volatile boolean jogo_rodando;
    private volatile boolean comecou_partida;
    private Thread thread_jogo;

    int x_inicial_jogador;
    int y_inicial_jogador;

    private PainelJogo painel;

    private final Random randomico = new Random();
    
    private final Mapa mapa;
    private final Jogador jogador;
    private final InputTeclado input_teclado;
    private final Consumo consumo;
    private final Fome fome;

    private final List<Monstro> lista_monstros;

    private final List<Serra> lista_serras;

    private final List<Desenhavel> lista_elementos_desenhaveis;

    public Game() {
        this.jogo_rodando = false;
        this.comecou_partida = false;

        this.mapa = new MapaBasico(LARGURA_JANELA, ALTURA_JANELA);

        x_inicial_jogador = (mapa.getCoordX() + ((mapa.getLargura() - JogadorBasico.LARGURA_JOGADOR) / 2));
        y_inicial_jogador = (mapa.getCoordY() + ((mapa.getAltura() - JogadorBasico.ALTURA_JOGADOR) / 2));

        this.jogador = new JogadorBasico(x_inicial_jogador, y_inicial_jogador);

        this.consumo = new Consumo();
        this.fome = new Fome();

        this.lista_elementos_desenhaveis = new ArrayList<>();
        this.popularListaDesenhaveis();

        this.input_teclado = new InputTeclado(jogador);

        this.lista_monstros = new ArrayList<>();
        this.popularListaMonstros();

        this.lista_serras = new ArrayList<>();
        this.gerarSerras();
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
        this.jogador.movimentar(this.mapa);

        if(!this.comecou_partida) {
            if(this.x_inicial_jogador == this.jogador.getCoordX() && this.y_inicial_jogador == this.jogador.getCoordY()) {
                return;
            }

            this.comecou_partida = true;
        }

        gerenciarMonstros();
        gerenciarRespawn();
        gerenciarSerras();
        gerenciarFome();
    }

    private void gerenciarFome() {
        this.fome.addTempoFomeDecorridoNanos((long) NANOS_POR_TICK);

        long tempo_morte_fome_nanos = Fome.TEMPO_MORTE_FOME_MS * NANOS_POR_MS;

        if(this.fome.getTempoFomeDecorridoNanos() >= tempo_morte_fome_nanos) {
            matarJogador();
        }
    }

    private void gerenciarRespawn() {
        long tempo_respawn_nanos = MonstroBasico.TEMPO_RESPAWN_MONSTROS_MS * NANOS_POR_MS;

        for(Monstro monstro : this.lista_monstros) {
            if(monstro.getEstaVivo()) {
                continue;
            }

            monstro.addTempoRespawnDecorridoNanos((long) NANOS_POR_TICK);

            if(monstro.getTempoRespawnDecorridoNanos() >= tempo_respawn_nanos) {
                nascerMonstro(monstro);
            }
        }
    }

    private void atualizarPainel() {
        if(painel == null) {
            return;
        }

        painel.repaint();
    }

    private void gerenciarMonstros() {
        for(Monstro monstro : this.lista_monstros) {
            if(!monstro.getEstaVivo()) {
                continue;
            }

            monstro.fugir(jogador);
            monstro.movimentar(mapa);
            if(monstro.checarColisaoCirculos(monstro, this.jogador)) {
                consumirMonstro();

                monstro.morrer();
            }
        }
    }

    private void gerarMonstros() {
        Monstro monstro = new MonstroBasico(0, 0);
        
        nascerMonstro(monstro);

        this.lista_monstros.add(monstro);
        synchronized (this.lista_elementos_desenhaveis) {
            this.lista_elementos_desenhaveis.addFirst(monstro);
        }
    }

    private void nascerMonstro(Monstro monstro) {
        do {
            int coord_x_aleatoria = (this.mapa.getCoordX() + this.randomico.nextInt(this.mapa.getLargura() - MonstroBasico.LARGURA_MONSTRO));
            int coord_y_aleatoria = (this.mapa.getCoordY() + this.randomico.nextInt(this.mapa.getAltura() - MonstroBasico.ALTURA_MONSTRO));

            monstro.nascer(coord_x_aleatoria, coord_y_aleatoria);

        } while(monstro.checarColisaoCirculos(monstro, this.jogador));
    }

    private void popularListaMonstros() {
        while(this.lista_monstros.size() < QUANTIDADE_MONSTROS) {
            gerarMonstros();
        }
    }

    private void consumirMonstro() {
        this.consumo.consumir();
        this.fome.saciarFome();
    }

    private void gerarSerras() {
        Serra serra;
        
        do {
        int coord_x_aleatoria;
        int coord_y_aleatoria = ((this.mapa.getCoordY() + 1) + (this.randomico.nextInt((this.mapa.getAltura() - SerraPrimeira.ALTURA_SERRA) - 1)));

        if(this.randomico.nextBoolean()) {
            coord_x_aleatoria = this.mapa.getCoordX();
        } else {
            coord_x_aleatoria = ((this.mapa.getCoordX() + this.mapa.getLargura()) - SerraPrimeira.LARGURA_SERRA);
        }
        
        serra = new SerraPrimeira(coord_x_aleatoria, coord_y_aleatoria, this.randomico.nextBoolean() ? 1 : -1);

        } while(serra.checarColisaoCirculos(serra, this.jogador));
        
        this.lista_serras.add(serra);
        synchronized (this.lista_elementos_desenhaveis) {
            this.lista_elementos_desenhaveis.add(serra);
        }
    }

    private void gerenciarSerras() {
        for(Serra serra : lista_serras) {
            serra.movimentar(mapa);

            if(serra.checarColisaoCirculos(serra, this.jogador)) {
                matarJogador();
            }
        }
    }

    void matarJogador() {
        this.parar();
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
        this.lista_elementos_desenhaveis.addFirst(mapa);
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