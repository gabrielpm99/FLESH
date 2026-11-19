package main.jogador;

import main.mapa.Mapa;

public class Jogador {
    private int largura_jogador;
    private int altura_jogador;
    
    private int coord_x;
    private int coord_y;

    private int velocidade_jogador;

    private boolean movimentando_cima = false;
    private boolean movimentando_baixo = false;
    private boolean movimentando_esquerda = false;
    private boolean movimentando_direita = false;

    public Jogador(int largura, int altura, int velocidade) {
        if(largura <= 0 || altura <= 0) {
            throw new IllegalArgumentException("A largura e altura do jogador devem ser positivas");
        }
        
        this.largura_jogador = largura;
        this.altura_jogador = altura;
        this.velocidade_jogador = velocidade;
    }


    public void movimentarJogador(Mapa mapa) {
        if(this.movimentando_cima) {
            if(!(mapa.checarColisaoMapa(this.coord_x, (this.coord_y - this.velocidade_jogador)))) {
                this.coord_y -= this.velocidade_jogador;
            }
        }

        if(this.movimentando_baixo) {
            if(!(mapa.checarColisaoMapa(this.coord_x, ((this.coord_y + this.largura_jogador) + this.velocidade_jogador)))) {
                this.coord_y += this.velocidade_jogador;
            }
        }

        if(this.movimentando_esquerda) {
            if(!(mapa.checarColisaoMapa((this.coord_x - this.velocidade_jogador), this.coord_y))) {
                this.coord_x -= this.velocidade_jogador;
            }
        }

        if(this.movimentando_direita) {
            if(!(mapa.checarColisaoMapa(((this.coord_x + this.largura_jogador) + this.velocidade_jogador), this.coord_y))) {
                this.coord_x += this.velocidade_jogador;
            }
        }
    }
    

    public void setPosicaoInicialJogador(int x_posicao_inicial, int y_posicao_inicial) {
        this.coord_x = x_posicao_inicial;
        this.coord_y = y_posicao_inicial;
    }


    public int getAlturaJogador() {
        return this.altura_jogador;
    }

    public int getLarguraJogador() {
        return this.largura_jogador;
    }

    public int getCoordXJogador() {
        return this.coord_x;
    }

    public int getCoordYJogador() {
        return this.coord_y;
    }


    public void moverParaCima(boolean podeMover) {
        this.movimentando_cima = podeMover;
    }

    public void moverParaBaixo(boolean podeMover) {
        this.movimentando_baixo = podeMover;
    }

    public void moverParaEsquerda(boolean podeMover) {
        this.movimentando_esquerda = podeMover;
    }

    public void moverParaDireita(boolean podeMover) {
        this.movimentando_direita = podeMover;
    }
}
