package main.monstros;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import main.entidade.Entidade;
import main.jogador.Jogador;
import main.mapa.Mapa;

public abstract class Monstro extends Entidade {
    private final Random randomico;

    private final int distancia_fuga;

    private boolean esta_vivo;

    private long tempo_respawn_decorrido_nanos;

    private int direcao_eixo_x;
    private int direcao_eixo_y;

    private boolean esta_fugindo;

    private int chance_troca_direcao;
    
    public Monstro(int largura, int altura, int velocidade, int coord_x_inicial, int coord_y_inicial, int distancia_fuga, int chance_troca_direcao) {
        super(largura, altura, velocidade, coord_x_inicial, coord_y_inicial);
        
        if(distancia_fuga <= 0) {
            throw new IllegalArgumentException("A distância de fuga não pode ser menor ou igual a 0");
        }
        

        this.distancia_fuga = distancia_fuga;

        this.tempo_respawn_decorrido_nanos = 0;

        this.esta_vivo = true;

        this.randomico = new Random();

        this.esta_fugindo = false;

        this.chance_troca_direcao = chance_troca_direcao;

        sortearDirecao();
    }


    @Override
    public void movimentar(Mapa mapa) {
        if(mapa == null) {
            throw new IllegalArgumentException("A instância de mapa não pode ser nula");
        }

        
        if((!this.esta_fugindo) && (this.randomico.nextInt(chance_troca_direcao) == 0)) {
            sortearDirecao();
        }


        this.coord_x += this.direcao_eixo_x * this.velocidade;
        if(mapa.checarColisaoRetangulo(this)) {
            this.coord_x -= this.direcao_eixo_x * this.velocidade;
            
            if(!this.esta_fugindo) {
                sortearDirecao();
            }
        }

        this.coord_y += this.direcao_eixo_y * this.velocidade;
        if(mapa.checarColisaoRetangulo(this)) {
            this.coord_y -= this.direcao_eixo_y * this.velocidade;
            
            if(!this.esta_fugindo) {
                sortearDirecao();
            }
        }
    }


    private void sortearDirecao() {
        this.direcao_eixo_x = this.randomico.nextInt(3) - 1;
        this.direcao_eixo_y = this.randomico.nextInt(3) - 1;

        if(direcao_eixo_x == 0 && direcao_eixo_y == 0) {
            direcao_eixo_x = 1;
        }
    }


    public void fugir(Jogador jogador) {
        if(jogador == null) {
            throw new IllegalArgumentException("A instância de jogador não pode ser nula");
        }

        int distancia_eixo_x = Math.abs(this.coord_x - jogador.getCoordX());
        int distancia_eixo_y = Math.abs(this.coord_y - jogador.getCoordY());

        if((distancia_eixo_x <= this.distancia_fuga) && (distancia_eixo_y <= this.distancia_fuga)) {
            this.esta_fugindo = true;

            if(this.coord_x < jogador.getCoordX()) {
                this.direcao_eixo_x = -1;
            } 
            else if(this.coord_x > jogador.getCoordX()) {
                this.direcao_eixo_x = 1;
            }


            if(this.coord_y < jogador.getCoordY()) {
                this.direcao_eixo_y = -1;
            } 
            else if(this.coord_y > jogador.getCoordY()) {
                this.direcao_eixo_y = 1;
            }
        } else {
            this.esta_fugindo = false;
        }
    }


    public void addTempoRespawnDecorridoNanos(long nanos_decorridos) {
        this.tempo_respawn_decorrido_nanos += nanos_decorridos;
    }

    public long getTempoRespawnDecorridoNanos() {
        return this.tempo_respawn_decorrido_nanos;
    }

    public void resetTempoRespawnDecorridoNanos() {
        this.tempo_respawn_decorrido_nanos = 0;
    }


    public boolean getEstaVivo() {
        return this.esta_vivo;
    }

    public void morrer() {
        this.esta_vivo = false;
    }

    public void nascer(int coord_x_nova, int coord_y_nova) {
        this.esta_vivo = true;
        this.resetTempoRespawnDecorridoNanos();

        this.coord_x = coord_x_nova;
        this.coord_y = coord_y_nova;
    }


    @Override
    public void desenhar(Graphics2D g2d) {
        if(g2d == null) {
            throw new IllegalArgumentException("A instância de g2d não pode ser nula");
        }
        
        if(!this.esta_vivo){
            return;
        }
        
        g2d.setColor(Color.RED);
        g2d.fillOval(this.coord_x, this.coord_y, this.largura, this.altura);
    }
}
