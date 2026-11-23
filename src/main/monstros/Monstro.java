package main.monstros;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import main.entidade.Entidade;
import main.jogador.Jogador;
import main.mapa.Mapa;

public class Monstro extends Entidade {
    private static final int CHANCE_TROCA_DIRECAO = 50;


    private final Random randomico;

    private final int distancia_fuga;

    private int direcao_eixo_x;
    private int direcao_eixo_y;

    private boolean esta_fugindo;
    
    public Monstro(int largura, int altura, int velocidade, int coord_x_inicial, int coord_y_inicial, int distancia_fuga) {
        super(largura, altura, velocidade, coord_x_inicial, coord_y_inicial);

        this.distancia_fuga = distancia_fuga;

        this.randomico = new Random();

        this.esta_fugindo = false;

        sortearDirecao();
    }


    @Override
    public void movimentar(Mapa mapa) {
        if(mapa == null) {
            throw new IllegalArgumentException("A instância de mapa não pode ser nula");
        }

        
        if((!this.esta_fugindo) && (this.randomico.nextInt(CHANCE_TROCA_DIRECAO) == 0)) {
            sortearDirecao();
        }


        this.coord_x += this.direcao_eixo_x * this.velocidade;
        if(mapa.checarColisao(this)) {
            this.coord_x -= this.direcao_eixo_x * this.velocidade;
            
            if(!this.esta_fugindo) {
                sortearDirecao();
            }
        }

        this.coord_y += this.direcao_eixo_y * this.velocidade;
        if(mapa.checarColisao(this)) {
            this.coord_y -= this.direcao_eixo_y * this.velocidade;
            
            if(!this.esta_fugindo) {
                sortearDirecao();
            }
        }
    }


    private void sortearDirecao() {
        this.direcao_eixo_x = this.randomico.nextInt(3) - 1;
        this.direcao_eixo_y = this.randomico.nextInt(3) - 1;

        if(direcao_eixo_x == 0 && direcao_eixo_y == 0) {
            direcao_eixo_x = 1;
        }
    }


    public void fugir(Jogador jogador) {
        int distancia_eixo_x = Math.abs(this.coord_x - jogador.getCoordX());
        int distancia_eixo_y = Math.abs(this.coord_y - jogador.getCoordY());

        if((distancia_eixo_x <= this.distancia_fuga) && (distancia_eixo_y <= this.distancia_fuga)) {
            this.esta_fugindo = true;

            if(this.coord_x < jogador.getCoordX()) {
                this.direcao_eixo_x = -1;
            } 
            else if(this.coord_x > jogador.getCoordX()) {
                this.direcao_eixo_x = 1;
            }


            if(this.coord_y < jogador.getCoordY()) {
                this.direcao_eixo_y = -1;
            } 
            else if(this.coord_y > jogador.getCoordY()) {
                this.direcao_eixo_y = 1;
            }
        } else {
            this.esta_fugindo = false;
        }
    }


    @Override
    public void desenhar(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fillRect(this.coord_x, this.coord_y, this.largura, this.altura);
    }
}
