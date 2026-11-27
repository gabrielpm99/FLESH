package main.mapa;

public class MapaBasico extends Mapa {
    public static final int LARGURA_MAPA = 600;
    public static final int ALTURA_MAPA = 600;

    public MapaBasico(int largura_janela, int altura_janela) {
        super(LARGURA_MAPA, ALTURA_MAPA, largura_janela, altura_janela);
    }
}
