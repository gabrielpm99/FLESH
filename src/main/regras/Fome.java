package main.regras;

public class Fome {
    public static final int TEMPO_MORTE_FOME_MS = 5000;

    private long tempo_fome_decorrido_nanos;

    public Fome() {
        this.tempo_fome_decorrido_nanos = 0;
    }

    public void saciarFome() {
        this.tempo_fome_decorrido_nanos = 0;
    }

    public long getTempoFomeDecorridoNanos() {
        return this.tempo_fome_decorrido_nanos;
    }

    public void addTempoFomeDecorridoNanos(long nanos_decorridos) {
        this.tempo_fome_decorrido_nanos += nanos_decorridos;
    }
}
