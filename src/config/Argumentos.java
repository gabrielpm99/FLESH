package config;

public final class Argumentos {
    private static String[] valor = new String[0];

    private Argumentos() {} // pra evitar new Argumentos()

    public static String[] obter() { return valor.clone(); }
    public static void definir(String[] args) { valor = args.clone(); }
}
