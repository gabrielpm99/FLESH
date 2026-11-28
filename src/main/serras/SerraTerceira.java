package main.serras;

import java.util.Random;

import main.mapa.Mapa;

public class SerraTerceira extends Serra {
    public static final int DIAMETRO_SERRA = 64;
    public static final int ALTURA_SERRA = DIAMETRO_SERRA;
    public static final int LARGURA_SERRA = DIAMETRO_SERRA;
    public static final int VELOCIDADE_MIN_SERRA = 2;
    public static final int VELOCIDADE_MAX_SERRA = 6;


    private final Random randomico_vel;
    private int velocidade = VELOCIDADE_MIN_SERRA;
    
    public SerraTerceira(int coord_x_inicial, int coord_y_inicial, int direcao_inicial) {
        super(LARGURA_SERRA, ALTURA_SERRA, VELOCIDADE_MIN_SERRA, coord_x_inicial, coord_y_inicial, direcao_inicial);
        this.randomico_vel = new Random();
    }

    @Override
    public void movimentar(Mapa mapa) {
        if(mapa == null) {
            throw new IllegalArgumentException("A instância de mapa não pode ser nula");
        }

        this.coord_x += (this.velocidade * this.direcao_eixo_x);
        if(mapa.checarColisaoRetangulo(this)) {
            this.direcao_eixo_x *= -1;
            sortearVelocidade();
            this.coord_x += (this.velocidade * this.direcao_eixo_x);
        }

        this.coord_y += (this.velocidade * this.direcao_eixo_y);
        if(mapa.checarColisaoRetangulo(this)) {
            this.direcao_eixo_y *= -1;
            sortearVelocidade();
            this.coord_y += (this.velocidade * this.direcao_eixo_y);
        }
    }

    private void sortearVelocidade(){
        this.velocidade = this.randomico_vel.nextInt((VELOCIDADE_MAX_SERRA - VELOCIDADE_MIN_SERRA) + 1) + VELOCIDADE_MIN_SERRA;
    }

}