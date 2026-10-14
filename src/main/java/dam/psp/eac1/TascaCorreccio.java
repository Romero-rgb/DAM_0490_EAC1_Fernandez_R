package dam.psp.eac1;

import java.util.concurrent.Callable;

public class TascaCorreccio implements Callable<ResultatCorreccio> {
    private final Lliurament lliurament;
    private final MarcadorCorreccions marcador;

    public TascaCorreccio(Lliurament lliurament, MarcadorCorreccions marcador) {
        if (lliurament == null) {
            throw new IllegalArgumentException("El lliurament no pot ser null.");
        }
        if (marcador == null) {
            throw new IllegalArgumentException("El marcador no pot ser null.");
        }
        this.lliurament = lliurament;
        this.marcador = marcador;
    }

    @Override
    public ResultatCorreccio call() {
        long inici = System.currentTimeMillis();

        dormir(lliurament.getTempsSimulatMs());

        CorrectorLliurament corrector = new CorrectorLliurament();
        boolean correcte = corrector.validar(lliurament.getRuta());

        long durada = System.currentTimeMillis() - inici;
        String nomFil = Thread.currentThread().getName();
        String missatge = correcte ? "Lliurament correcte" : "Lliurament amb errors";

        ResultatCorreccio resultat = new ResultatCorreccio(
                lliurament.getAlumneId(),
                correcte,
                durada,
                missatge,
                nomFil
        );

        marcador.registrar(resultat);

        return resultat;
    }

    private void dormir(long tempsMs) {
        try {
            Thread.sleep(Math.max(0L, tempsMs));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
