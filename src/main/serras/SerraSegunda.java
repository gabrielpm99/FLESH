package main.serras;

public class SerraSegunda extends Serra {
    public static final int DIAMETRO_SERRA = 128;
    public static final int ALTURA_SERRA = DIAMETRO_SERRA;
    public static final int LARGURA_SERRA = DIAMETRO_SERRA;
    public static final int VELOCIDADE_SERRA = 3;

    
    public SerraSegunda(int coord_x_inicial, int coord_y_inicial, int direcao_inicial) {
        super(LARGURA_SERRA, ALTURA_SERRA, -(VELOCIDADE_SERRA), coord_x_inicial, coord_y_inicial, direcao_inicial);
    }
}
