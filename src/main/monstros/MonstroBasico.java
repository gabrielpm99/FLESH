package main.monstros;

public class MonstroBasico extends Monstro {
    public static final int DIAMETRO_MONSTRO = 24;
    public static final int ALTURA_MONSTRO = DIAMETRO_MONSTRO;
    public static final int LARGURA_MONSTRO = DIAMETRO_MONSTRO;
    public static final int VELOCIDADE_MONSTRO = 1;
    public static final int DISTANCIA_DE_FUGA = 75;
    private static final int CHANCE_TROCA_DIRECAO = 50;

    public static final int TEMPO_RESPAWN_MONSTROS_MS = 3000;

    public MonstroBasico(int coord_x_inicial, int coord_y_inicial) {
        super(LARGURA_MONSTRO, ALTURA_MONSTRO, VELOCIDADE_MONSTRO, coord_x_inicial, coord_y_inicial, DISTANCIA_DE_FUGA, CHANCE_TROCA_DIRECAO);
    }
}
