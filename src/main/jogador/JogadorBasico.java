package main.jogador;

public class JogadorBasico extends Jogador {
    public static final int DIAMETRO_JOGADOR = 16;
    public static final int ALTURA_JOGADOR = DIAMETRO_JOGADOR;
    public static final int LARGURA_JOGADOR = DIAMETRO_JOGADOR;
    public static final int VELOCIDADE_JOGADOR = 4;


    public JogadorBasico(int coord_x_inicial, int coord_y_inicial) {
        super(LARGURA_JOGADOR, ALTURA_JOGADOR, VELOCIDADE_JOGADOR, coord_x_inicial, coord_y_inicial);
    }
}
