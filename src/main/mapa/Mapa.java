package main.mapa;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import interfaces.Colidivel;
import interfaces.Desenhavel;
import main.entidade.Entidade;

public class Mapa implements Colidivel, Desenhavel {
    private final int largura;
    private final int altura;

    private final int coord_x;
    private final int coord_y;

    public Mapa(int largura, int altura, int largura_janela, int altura_janela) {
        if(largura <= 0 || altura <= 0) {
            throw new IllegalArgumentException("A largura e altura do mapa devem ser valores positivos");
        }
        if(largura > largura_janela || altura > altura_janela) {
            throw new IllegalArgumentException("O tamanho do mapa deve ser menor do que o tamanho da janela");
        }

        this.largura = largura;
        this.altura = altura;

        this.coord_x = (largura_janela - largura) / 2;
        this.coord_y = (altura_janela - altura) / 2;
    }


    @Override
    public boolean checarColisaoRetangulo(Entidade entidade) {
        if(entidade == null) {
            throw new IllegalArgumentException("A entidade colidível não pode ser nula");
        }

        return !((this.getArea()).contains(entidade.getCoordX(), entidade.getCoordY(), entidade.getLargura(), entidade.getAltura()));
    }


    @Override
    public Rectangle getArea() {
        return new Rectangle(this.coord_x, this.coord_y, this.largura, this.altura);
    }

    public int getCoordX() {
        return this.coord_x;
    }

    public int getCoordY() {
        return this.coord_y;
    }

    public int getLargura() {
        return this.largura;
    }

    public int getAltura() {
        return this.altura;
    }


    @Override
    public void desenhar(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(this.coord_x, this.coord_y, this.largura, this.altura);
    }
}
