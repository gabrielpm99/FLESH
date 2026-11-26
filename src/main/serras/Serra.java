package main.serras;

import java.awt.Color;
import java.awt.Graphics2D;

import main.entidade.Entidade;
import main.mapa.Mapa;

public class Serra extends Entidade {
    private int direcao_eixo_x;
    private int direcao_eixo_y;


    public Serra(int largura, int altura, int velocidade, int coord_x_inicial, int coord_y_inicial) {
        super(largura, altura, velocidade, coord_x_inicial, coord_y_inicial);
    }


    @Override
    public void movimentar(Mapa mapa) {
        
    }

    @Override
    public void desenhar(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.fillRect(this.coord_x, this.coord_y, this.largura, this.altura);
    }
}
