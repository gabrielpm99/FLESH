package interfaces;

import java.awt.Rectangle;
import java.awt.Shape;

import main.entidade.Entidade;

public interface Colidivel {
    Shape getArea();
    
    default boolean checarColisaoRetangulo(Entidade entidade) {
        if(entidade == null) {
            throw new IllegalArgumentException("A entidade colidível não pode ser nula");
        }

        return (this.getArea()).intersects((Rectangle) entidade.getArea());
    }

    default boolean checarColisaoCirculos(Entidade entidade_1, Entidade entidade_2) {
        double raio_elipse_1 = entidade_1.getLargura() / 2;
        double raio_elipse_2 = entidade_2.getLargura() / 2;
        
        double distancia_elipses_x = entidade_1.getCoordXCentro() - entidade_2.getCoordXCentro();
        double distancia_elipses_y = entidade_1.getCoordYCentro() - entidade_2.getCoordYCentro();

        double distancia_elipses_ao_quadrado = (distancia_elipses_x * distancia_elipses_x) + (distancia_elipses_y * distancia_elipses_y);

        double soma_raios_elipses_ao_quadrado = (raio_elipse_1 + raio_elipse_2) * (raio_elipse_1 + raio_elipse_2);

        return distancia_elipses_ao_quadrado <= soma_raios_elipses_ao_quadrado;
    }
}
