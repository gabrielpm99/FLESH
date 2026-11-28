package main.serras;

import java.awt.Color;
import java.awt.Graphics2D;

public class SerraPrimeira extends Serra {
    public static final int DIAMETRO_SERRA = 136;
    public static final int ALTURA_SERRA = DIAMETRO_SERRA;
    public static final int LARGURA_SERRA = DIAMETRO_SERRA;
    public static final int VELOCIDADE_SERRA = 3;

    
    public SerraPrimeira(int coord_x_inicial, int coord_y_inicial, int direcao_inicial) {
        super(LARGURA_SERRA, ALTURA_SERRA, VELOCIDADE_SERRA, coord_x_inicial, coord_y_inicial, direcao_inicial);
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
