package main.entidade;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import main.mapa.Mapa;

import interfaces.Colidivel;
import interfaces.Desenhavel;

public abstract class Entidade implements Colidivel, Desenhavel {
    protected final int largura;
    protected final int altura;

    protected int coord_x;
    protected int coord_y;
    protected int velocidade;


    public Entidade(int largura, int altura, int velocidade, int coord_x_inicial, int coord_y_inicial) {
        if(largura <= 0 || altura <= 0) {
            throw new IllegalArgumentException("A largura e altura devem ser positivas");
        }
        if(velocidade <= 0) {
            throw new IllegalArgumentException("A velocidade deve ser positiva");
        }

        
        this.largura = largura;
        this.altura = altura;
        this.velocidade = velocidade;
        this.coord_x = coord_x_inicial;
        this.coord_y = coord_y_inicial;
    }


    public abstract void movimentar(Mapa mapa);

    @Override
    public Shape getArea() {
        return new Ellipse2D.Double(this.coord_x, this.coord_y, this.largura, this.altura);
    }

    public int getAltura() {
        return this.altura;
    }

    public int getLargura() {
        return this.largura;
    }

    public int getCoordXCentro() {
        return this.coord_x + (this.largura / 2);
    }

    public int getCoordYCentro() {
        return this.coord_y + (this.altura / 2);
    }

    public int getCoordX() {
        return this.coord_x;
    }

    public int getCoordY() {
        return this.coord_y;
    }
}
