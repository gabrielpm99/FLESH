package main.serras;

import java.util.Random;

import java.awt.Color;
import java.awt.Graphics2D;

import main.mapa.Mapa;

public class SerraQuarta extends Serra {
    public static final int DIAMETRO_SERRA = 88;
    public static final int ALTURA_SERRA = DIAMETRO_SERRA;
    public static final int LARGURA_SERRA = DIAMETRO_SERRA;
    public static final int VELOCIDADE_SERRA = -2;

    private final Random randomico_dir;

    public SerraQuarta(int coord_x_inicial, int coord_y_inicial, int direcao_inicial) {
        super(LARGURA_SERRA, ALTURA_SERRA, VELOCIDADE_SERRA, coord_x_inicial, coord_y_inicial, direcao_inicial);
        this.randomico_dir = new Random();
    }
    
    @Override
    public void movimentar(Mapa mapa) {
        if(mapa == null) {
            throw new IllegalArgumentException("A instância de mapa não pode ser nula");
        }

        this.coord_x += (this.velocidade * this.direcao_eixo_x);
        if(mapa.checarColisaoRetangulo(this)) {
            this.direcao_eixo_x *= -1;
            sortearDirecaoEixoY();
            this.coord_x += (this.velocidade * this.direcao_eixo_x);
        }

        this.coord_y += (this.velocidade * this.direcao_eixo_y);
        if(mapa.checarColisaoRetangulo(this)) {
            this.direcao_eixo_y *= -1;
            sortearDirecaoEixoX();
            this.coord_y += (this.velocidade * this.direcao_eixo_y);
        }
    }

    private void sortearDirecaoEixoX(){
        this.direcao_eixo_x = this.randomico_dir.nextBoolean() ? 1 : -1;
    }

    private void sortearDirecaoEixoY(){
        this.direcao_eixo_y = this.randomico_dir.nextBoolean() ? 1 : -1;
    }

    @Override
    public void desenhar(Graphics2D g2d) {
        if(g2d == null) {
            throw new IllegalArgumentException("A instância de g2d não pode ser nula");
        }
        
        g2d.setColor(Color.lightGray);
        g2d.fillOval(this.coord_x, this.coord_y, this.largura, this.altura);
    }
    
}
