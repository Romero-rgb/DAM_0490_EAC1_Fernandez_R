package dam.psp.eac1;

import java.util.Objects;

public class ResultatCorreccio {
    private final String alumneId;
    private final boolean correcte;
    private final long tempsProcessamentMs;
    private final String missatge;
    private final String nomFil;

    public ResultatCorreccio(String alumneId, boolean correcte, long tempsProcessamentMs, String missatge, String nomFil) {
        this.alumneId = Objects.requireNonNull(alumneId, "alumneId");
        if (alumneId.isBlank()) {
            throw new IllegalArgumentException("L'identificador de l'alumne no pot ser buit.");
        }
        this.correcte = correcte;
        if (tempsProcessamentMs < 0L) {
            throw new IllegalArgumentException("El temps de processament no pot ser negatiu.");
        }
        this.tempsProcessamentMs = tempsProcessamentMs;
        this.missatge = Objects.requireNonNull(missatge, "missatge");
        if (missatge.isBlank()) {
            throw new IllegalArgumentException("El missatge no pot ser buit.");
        }
        this.nomFil = Objects.requireNonNull(nomFil, "nomFil");
        if (nomFil.isBlank()) {
            throw new IllegalArgumentException("El nom del fil no pot ser buit.");
        }
    }

    public String getAlumneId() {
        return alumneId;
    }

    public boolean isCorrecte() {
        return correcte;
    }

    public long getTempsProcessamentMs() {
        return tempsProcessamentMs;
    }

    public String getMissatge() {
        return missatge;
    }

    public String getNomFil() {
        return nomFil;
    }

    @Override
    public String toString() {
        return "ResultatCorreccio{" +
                "alumneId='" + alumneId + '\'' +
                ", correcte=" + correcte +
                ", tempsProcessamentMs=" + tempsProcessamentMs +
                ", missatge='" + missatge + '\'' +
                ", nomFil='" + nomFil + '\'' +
                '}';
    }
}
