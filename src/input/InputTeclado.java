package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.jogador.Jogador;

public class InputTeclado implements KeyListener {
    private final Jogador jogador;

    public InputTeclado(Jogador jogador) {
        if(jogador == null) {
            throw new IllegalArgumentException("O jogador não pode ser nulo");
        }

        this.jogador = jogador;
    }


    @Override 
    public void keyPressed(KeyEvent e) {
        mapearTeclas(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mapearTeclas(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {}


    private void mapearTeclas(int tecla, boolean podeMover) {
        switch(tecla) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                jogador.moverParaCima(podeMover);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                jogador.moverParaBaixo(podeMover);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                jogador.moverParaEsquerda(podeMover);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                jogador.moverParaDireita(podeMover);
                break;
            default:
                break;
        }
    }
}
