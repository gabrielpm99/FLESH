package main.regras;

public class Consumo {
    private static int ESTAGIO_1 = 15;
    private static int ESTAGIO_2 = 30;
    private static int ESTAGIO_3 = 45;


    private int monstros_consumidos;

    public Consumo() {
        this.monstros_consumidos = 0;
    }


    public void consumir() {
        this.monstros_consumidos += 1;
    }

    public int getEstagio() {
        if(this.monstros_consumidos < ESTAGIO_1) {
            return 0;
        } else if (this.monstros_consumidos < ESTAGIO_2) {
            return 1;
        } else if (this.monstros_consumidos < ESTAGIO_3) {
            return 2;
        } else {
            return 3;
        }
    }

    public int getMonstrosConsumidos() {
        return this.monstros_consumidos;
    }
}
