package dam.psp.eac1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CorrectorExecutor {
    public static final long TEMPS_MAXIM_TASCA_SEGONS = 3L;
    public static final long TEMPS_MAXIM_TANCAMENT_SEGONS = 5L;

    public static void main(String[] args) throws Exception {
        int midaPool = llegirMidaPool(args);

        CorrectorExecutor corrector = new CorrectorExecutor();
        List<Lliurament> lliuraments = CarregadorLliuraments.carregar();
        InformeExecucio informe = corrector.corregir(lliuraments, midaPool);

        System.out.println(informe);
    }

    private static int llegirMidaPool(String[] args) {
        if (args == null || args.length == 0) {
            return 3;
        }
        if (args.length > 1 || args[0] == null || args[0].isBlank()) {
            throw new IllegalArgumentException("Cal indicar com a màxim un argument amb la mida del pool.");
        }

        int midaPool = Integer.parseInt(args[0]);
        if (midaPool <= 0) {
            throw new IllegalArgumentException("La mida del pool ha de ser positiva.");
        }
        return midaPool;
    }

    public InformeExecucio corregir(List<Lliurament> lliuraments, int midaPool) throws Exception {
        if (lliuraments == null || lliuraments.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException("La llista de lliuraments no pot ser null ni contenir valors null.");
        }
        if (midaPool <= 0) {
            throw new IllegalArgumentException("La mida del pool ha de ser positiva.");
        }

        long iniciPrograma = System.currentTimeMillis();
        MarcadorCorreccions marcador = new MarcadorCorreccions();
        ExecutorService executor = Executors.newFixedThreadPool(midaPool);
        List<ResultatCorreccio> resultats = new ArrayList<>();

        try {
            List<Future<ResultatCorreccio>> futures = enviarTasques(lliuraments, marcador, executor);
            resultats = recollirResultats(futures);
        } finally {
            tancarExecutor(executor);
        }

        long tempsRealMs = System.currentTimeMillis() - iniciPrograma;
        return new InformeExecucio(midaPool, tempsRealMs, marcador, resultats);
    }

    public List<Future<ResultatCorreccio>> enviarTasques(
            List<Lliurament> lliuraments,
            MarcadorCorreccions marcador,
            ExecutorService executor
    ) {
        // TODO Exercici 3:
        // Valideu que cap paràmetre sigui null.
        // Valideu que la llista de lliuraments no contingui valors null.
        // Envieu una TascaCorreccio al pool per cada Lliurament rebut.
        // Retorneu la llista de Future<ResultatCorreccio>.
        return new ArrayList<>();
    }

    public List<ResultatCorreccio> recollirResultats(List<Future<ResultatCorreccio>> futures) throws Exception {
        // TODO Exercici 3:
        // Valideu que la llista de futures no sigui null ni contingui valors null.
        // Recorreu la llista de Future.
        // Obteniu cada resultat amb un temps màxim TEMPS_MAXIM_TASCA_SEGONS.
        // Si una tasca supera el temps màxim, cancel·leu-la.
        // Afegiu només els resultats obtinguts correctament.
        return new ArrayList<>();
    }

    public void tancarExecutor(ExecutorService executor) throws InterruptedException {
        // TODO Exercici 3:
        // Valideu que executor no sigui null.
        // Demaneu el tancament ordenat de l'executor.
        // Espereu TEMPS_MAXIM_TANCAMENT_SEGONS.
        // Si encara hi ha tasques pendents, forceu el tancament.
    }

    public static class InformeExecucio {
        private final int midaPool;
        private final long tempsRealMs;
        private final MarcadorCorreccions marcador;
        private final List<ResultatCorreccio> resultats;

        public InformeExecucio(int midaPool, long tempsRealMs, MarcadorCorreccions marcador, List<ResultatCorreccio> resultats) {
            if (midaPool <= 0) {
                throw new IllegalArgumentException("La mida del pool ha de ser positiva.");
            }
            if (tempsRealMs < 0L) {
                throw new IllegalArgumentException("El temps real no pot ser negatiu.");
            }
            if (marcador == null) {
                throw new IllegalArgumentException("El marcador no pot ser null.");
            }
            if (resultats == null || resultats.stream().anyMatch(java.util.Objects::isNull)) {
                throw new IllegalArgumentException("La llista de resultats no pot ser null ni contenir valors null.");
            }
            this.midaPool = midaPool;
            this.tempsRealMs = tempsRealMs;
            this.marcador = marcador;
            this.resultats = List.copyOf(resultats);
        }

        public int getMidaPool() {
            return midaPool;
        }

        public long getTempsRealMs() {
            return tempsRealMs;
        }

        public MarcadorCorreccions getMarcador() {
            return marcador;
        }

        public List<ResultatCorreccio> getResultats() {
            return resultats;
        }

        @Override
        public String toString() {
            return "InformeExecucio{" +
                    "midaPool=" + midaPool +
                    ", tempsRealMs=" + tempsRealMs +
                    ", marcador=" + marcador.resum() +
                    ", resultats=" + resultats.size() +
                    '}';
        }
    }
}
