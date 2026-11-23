package interfaces;

import java.awt.Rectangle;

public interface Colidivel {
    Rectangle getArea();
    
    default boolean checarColisao(Colidivel entidade) {
        if(entidade == null) {
            throw new IllegalArgumentException("A entidade colidível não pode ser nula");
        }

        return (this.getArea()).intersects(entidade.getArea());
    }
}
