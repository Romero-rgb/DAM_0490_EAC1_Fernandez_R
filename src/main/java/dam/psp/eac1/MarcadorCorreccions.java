package dam.psp.eac1;

public class MarcadorCorreccions {
    private int totalProcessats;
    private int totalCorrectes;
    private int totalErrors;
    private long tempsTotalProcessamentMs;

    public void registrar(ResultatCorreccio resultat) {
        if (resultat == null) {
            throw new IllegalArgumentException("El resultat no pot ser null.");
        }

        // TODO Exercici 4: protegiu aquesta secció crítica.
        totalProcessats++;
        if (resultat.isCorrecte()) {
            totalCorrectes++;
        } else {
            totalErrors++;
        }
        tempsTotalProcessamentMs += resultat.getTempsProcessamentMs();
    }

    public int getTotalProcessats() {
        // TODO Exercici 4: valoreu si els getters també han d'estar sincronitzats.
        return totalProcessats;
    }

    public int getTotalCorrectes() {
        return totalCorrectes;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public long getTempsTotalProcessamentMs() {
        return tempsTotalProcessamentMs;
    }

    public String resum() {
        return "MarcadorCorreccions{" +
                "totalProcessats=" + totalProcessats +
                ", totalCorrectes=" + totalCorrectes +
                ", totalErrors=" + totalErrors +
                ", tempsTotalProcessamentMs=" + tempsTotalProcessamentMs +
                '}';
    }
}
