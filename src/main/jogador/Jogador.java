package main.jogador;

import main.entidade.Entidade;
import main.mapa.Mapa;

import java.awt.Color;
import java.awt.Graphics2D;

public class Jogador extends Entidade {
    private boolean movimentando_cima = false;
    private boolean movimentando_baixo = false;
    private boolean movimentando_esquerda = false;
    private boolean movimentando_direita = false;

    public Jogador(int largura, int altura, int velocidade, int coord_x_inicial, int coord_y_inicial) {
        super(largura, altura, velocidade, coord_x_inicial, coord_y_inicial);
    }


    @Override
    public void movimentar(Mapa mapa) {
        if(mapa == null) {
            throw new IllegalArgumentException("A instância de mapa não pode ser nula");
        }


        if(this.movimentando_cima) {
            this.coord_y -= this.velocidade;

            if(mapa.checarColisao(this)) {
                this.coord_y = mapa.getCoordY();
            }
        }

        if(this.movimentando_baixo) {
            this.coord_y += this.velocidade;

            if(mapa.checarColisao(this)) {
                this.coord_y = ((mapa.getCoordY() + mapa.getAltura()) - this.altura);
            }
        }

        if(this.movimentando_esquerda) {
            this.coord_x -= this.velocidade;

            if(mapa.checarColisao(this)) {
                this.coord_x = mapa.getCoordX();
            }
        }

        if(this.movimentando_direita) {
            this.coord_x += this.velocidade;

            if(mapa.checarColisao(this)) {
                this.coord_x = ((mapa.getCoordX() + mapa.getLargura()) - this.largura);
            }
        }
    }


    public void moverParaCima(boolean pode_mover) {
        this.movimentando_cima = pode_mover;
    }

    public void moverParaBaixo(boolean pode_mover) {
        this.movimentando_baixo = pode_mover;
    }

    public void moverParaEsquerda(boolean pode_mover) {
        this.movimentando_esquerda = pode_mover;
    }

    public void moverParaDireita(boolean pode_mover) {
        this.movimentando_direita = pode_mover;
    }


    @Override
    public void desenhar(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect(this.coord_x, this.coord_y, this.largura, this.altura);
    }
}
