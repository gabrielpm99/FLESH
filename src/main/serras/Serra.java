package main.serras;

import java.awt.Color;
import java.awt.Graphics2D;

import main.entidade.Entidade;
import main.mapa.Mapa;

public abstract class Serra extends Entidade {
    private int direcao_eixo_x;
    private int direcao_eixo_y;


    public Serra(int largura, int altura, int velocidade, int coord_x_inicial, int coord_y_inicial, int direcao_inicial) {
        super(largura, altura, velocidade, coord_x_inicial, coord_y_inicial);

        this.direcao_eixo_x = direcao_inicial;
        this.direcao_eixo_y = direcao_inicial;
    }


    @Override
    public void movimentar(Mapa mapa) {
        if(mapa == null) {
            throw new IllegalArgumentException("A instância de mapa não pode ser nula");
        }

        this.coord_x += (this.velocidade * this.direcao_eixo_x);
        if(mapa.checarColisaoRetangulo(this)) {
            this.direcao_eixo_x *= -1;
            this.coord_x += (this.velocidade * this.direcao_eixo_x);
        }

        this.coord_y += (this.velocidade * this.direcao_eixo_y);
        if(mapa.checarColisaoRetangulo(this)) {
            this.direcao_eixo_y *= -1;
            this.coord_y += (this.velocidade * this.direcao_eixo_y);
        }
    }

    @Override
    public void desenhar(Graphics2D g2d) {
        if(g2d == null) {
            throw new IllegalArgumentException("A instância de g2d não pode ser nula");
        }
        
        g2d.setColor(Color.GREEN);
        g2d.fillOval(this.coord_x, this.coord_y, this.largura, this.altura);
    }
}
