package main.mapa;

import java.awt.Rectangle;
import gui.PainelJogo;

public class Mapa {
    private int largura_mapa;
    private int altura_mapa;

    private int coord_x;
    private int coord_y;

    Rectangle mapa;

    public Mapa(int largura, int altura) {
        if(altura <= 0 || altura <= 0) {
            throw new IllegalArgumentException("A largura e altura do mapa devem ser valores positivos");
        }

        this.largura_mapa = largura;
        this.altura_mapa = altura;

        calcularCoordenadasCentralizadas();

        mapa = new Rectangle(this.coord_x, this.coord_y, this.largura_mapa, this.altura_mapa);
    }


    private void calcularCoordenadasCentralizadas() {
        if(this.largura_mapa > PainelJogo.LARGURA_JANELA || this.altura_mapa > PainelJogo.ALTURA_JANELA) {
            throw new IllegalArgumentException("O tamanho do mapa deve ser menor do que o tamanho da janela");
        }

        this.coord_x = (PainelJogo.LARGURA_JANELA - largura_mapa) / 2;
        this.coord_y = (PainelJogo.ALTURA_JANELA - altura_mapa) / 2;
    }

    public boolean checarColisaoMapa(int coord_x_entidade, int coord_y_entidade) {
        return !(mapa.contains(coord_x_entidade, coord_y_entidade));
    }


    public int getCoordXMapa() {
        return this.coord_x;
    }

    public int getCoordYMapa() {
        return this.coord_y;
    }

    public int getLarguraMapa() {
        return this.largura_mapa;
    }

    public int getAlturaMapa() {
        return this.altura_mapa;
    }
}
