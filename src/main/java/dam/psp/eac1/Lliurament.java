package dam.psp.eac1;

import java.nio.file.Path;
import java.util.Objects;

public class Lliurament {
    private final String alumneId;
    private final Path ruta;
    private final long tempsSimulatMs;

    public Lliurament(String alumneId, Path ruta, long tempsSimulatMs) {
        this.alumneId = Objects.requireNonNull(alumneId, "alumneId");
        if (alumneId.isBlank()) {
            throw new IllegalArgumentException("L'identificador de l'alumne no pot ser buit.");
        }
        this.ruta = Objects.requireNonNull(ruta, "ruta");
        if (tempsSimulatMs < 0L) {
            throw new IllegalArgumentException("El temps simulat no pot ser negatiu.");
        }
        this.tempsSimulatMs = tempsSimulatMs;
    }

    public String getAlumneId() {
        return alumneId;
    }

    public Path getRuta() {
        return ruta;
    }

    public long getTempsSimulatMs() {
        return tempsSimulatMs;
    }

    @Override
    public String toString() {
        return alumneId + " (" + ruta + ")";
    }
}
