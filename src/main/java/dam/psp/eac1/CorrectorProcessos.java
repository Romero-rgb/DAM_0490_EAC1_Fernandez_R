package dam.psp.eac1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CorrectorProcessos {
    public static final int CODI_TIMEOUT = -1;
    public static final long TEMPS_MAXIM_PROCES_SEGONS = 3L;

    private static final Path DIRECTORI_RESULTATS = Path.of("resultats");

    public static void main(String[] args) throws Exception {
        CorrectorProcessos corrector = new CorrectorProcessos();
        List<Lliurament> lliuraments = CarregadorLliuraments.carregar();
        List<ResultatProces> resultats = corrector.corregirLliuraments(lliuraments);
        corrector.mostrarResum(resultats);
    }

    public ResultatProces corregirUnLliurament(String alumneId, Path ruta)
            throws IOException, InterruptedException {
        if (alumneId == null || alumneId.isBlank()) {
            throw new IllegalArgumentException("L'identificador de l'alumne no pot ser buit.");
        }
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta del lliurament no pot ser null.");
        }

        Files.createDirectories(DIRECTORI_RESULTATS);
        Path fitxerSortida = DIRECTORI_RESULTATS.resolve(alumneId + ".txt");

        List<String> ordre = construirOrdreValidador(alumneId, ruta);
        ProcessBuilder pb = prepararProces(ordre, fitxerSortida);

        long inici = System.currentTimeMillis();
        Process proces = pb.start();
        int codiRetorn = esperarProces(proces);
        long durada = System.currentTimeMillis() - inici;

        return new ResultatProces(alumneId, codiRetorn, fitxerSortida, durada);
    }

    public List<String> construirOrdreValidador(String alumneId, Path ruta) {

        if (alumneId == null || alumneId.isBlank()) {
            throw new IllegalArgumentException ("L'id de l'alumne no pot ser null ni estar buit");
        }
        if (ruta == null) {
            throw new IllegalArgumentException ("La ruta es null");
        }

        String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        String classpath = System.getProperty("java.class.path");

        List<String> ordre = new java.util.ArrayList<>();
        ordre.add(java);
        ordre.add("-cp");
        ordre.add(classpath);
        ordre.add("dam.psp.eac1.ValidadorLliurament");
        ordre.add(alumneId);
        ordre.add(ruta.toString());

        return ordre;
    }

    public ProcessBuilder prepararProces(List<String> ordre, Path fitxerSortida) {

        if (ordre == null || ordre.isEmpty()) {
            throw new IllegalArgumentException("La llista d'ordre es null o es buida.");
        }
        for (String line : ordre) {
            if (line == null || line.isBlank() ) {
                throw new IllegalArgumentException("Ordre conté elements null o buits.");
            }
        }

        if (fitxerSortida == null) {
            throw new IllegalArgumentException("El fitxer de sortida es null");
        }

        ProcessBuilder pb = new ProcessBuilder(ordre);
        pb.redirectErrorStream(true);
        return  pb.redirectOutput(fitxerSortida.toFile());
    }

    public int esperarProces(Process proces) throws InterruptedException {

        if (proces == null) {
            throw new IllegalArgumentException("Proces es null");
        }
        boolean haAcabat = proces.waitFor(TEMPS_MAXIM_PROCES_SEGONS, TimeUnit.SECONDS);

        if (haAcabat) {
            int codi = proces.exitValue();
            return codi;
        }else {
            proces.destroyForcibly();
            proces.waitFor();
            return CODI_TIMEOUT;
        }


    }

    public List<ResultatProces> corregirLliuraments(List<Lliurament> lliuraments)
            throws IOException, InterruptedException {

        if (lliuraments == null || lliuraments.isEmpty()) {
            throw new IllegalArgumentException("La llista lliuraments es null o es buida.");
        }
        for (Lliurament lliurament : lliuraments){
            if (lliurament == null) {
                throw new IllegalArgumentException("La llista lliuraments conté elements null.");
            }
        }

        Files.createDirectories(DIRECTORI_RESULTATS);


        List<ProcesEnExecucio> procesEnExecucio = new ArrayList<>();

        for (Lliurament lliurament : lliuraments) {
            String alumneId = lliurament.getAlumneId();
            Path ruta = lliurament.getRuta();

            Path fitxerSortida = DIRECTORI_RESULTATS.resolve(alumneId + ".txt");

            List<String> ordre = construirOrdreValidador(alumneId, ruta);
            ProcessBuilder pb = prepararProces(ordre, fitxerSortida);

            long inici = System.currentTimeMillis();

            Process proces = pb.start();

            procesEnExecucio.add(new ProcesEnExecucio(alumneId, proces, fitxerSortida, inici));
        }

        List<ResultatProces> resultat = new ArrayList<>();
        for (ProcesEnExecucio proces : procesEnExecucio) {
            int codiSortida = esperarProces(proces.proces);
            long duracio = System.currentTimeMillis() - proces.iniciMs;
            resultat.add(new ResultatProces(proces.alumneId, codiSortida, proces.fitxerSortida, duracio));
        }

        return resultat;
    }

    public void mostrarResum(List<ResultatProces> resultats) {
        if (resultats == null || resultats.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException("La llista de resultats no pot ser null ni contenir valors null.");
        }

        long correctes = resultats.stream().filter(ResultatProces::isCorrecte).count();
        long errors = resultats.size() - correctes;

        System.out.println("TOTAL_PROCESSOS=" + resultats.size());
        System.out.println("CORRECTES=" + correctes);
        System.out.println("ERRORS=" + errors);
    }

    public static class ResultatProces {
        private final String alumneId;
        private final int codiRetorn;
        private final Path fitxerSortida;
        private final long duradaMs;

        public ResultatProces(String alumneId, int codiRetorn, Path fitxerSortida, long duradaMs) {
            if (alumneId == null || alumneId.isBlank()) {
                throw new IllegalArgumentException("L'identificador de l'alumne no pot ser buit.");
            }
            if (fitxerSortida == null) {
                throw new IllegalArgumentException("El fitxer de sortida no pot ser null.");
            }
            if (duradaMs < 0L) {
                throw new IllegalArgumentException("La durada no pot ser negativa.");
            }
            this.alumneId = alumneId;
            this.codiRetorn = codiRetorn;
            this.fitxerSortida = fitxerSortida;
            this.duradaMs = duradaMs;
        }

        public String getAlumneId() {
            return alumneId;
        }

        public int getCodiRetorn() {
            return codiRetorn;
        }

        public Path getFitxerSortida() {
            return fitxerSortida;
        }

        public long getDuradaMs() {
            return duradaMs;
        }

        public boolean isCorrecte() {
            return codiRetorn == ValidadorLliurament.CODI_CORRECTE;
        }

        @Override
        public String toString() {
            return "ResultatProces{" +
                    "alumneId='" + alumneId + '\'' +
                    ", codiRetorn=" + codiRetorn +
                    ", fitxerSortida=" + fitxerSortida +
                    ", duradaMs=" + duradaMs +
                    '}';
        }
    }

    private static class ProcesEnExecucio {
        private final String alumneId;
        private final Process proces;
        private final Path fitxerSortida;
        private final long iniciMs;

        private ProcesEnExecucio(String alumneId, Process proces, Path fitxerSortida, long iniciMs) {
            if (alumneId == null || alumneId.isBlank()) {
                throw new IllegalArgumentException("L'identificador de l'alumne no pot ser buit.");
            }
            if (proces == null) {
                throw new IllegalArgumentException("El procés no pot ser null.");
            }
            if (fitxerSortida == null) {
                throw new IllegalArgumentException("El fitxer de sortida no pot ser null.");
            }
            if (iniciMs < 0L) {
                throw new IllegalArgumentException("L'instant d'inici no pot ser negatiu.");
            }
            this.alumneId = alumneId;
            this.proces = proces;
            this.fitxerSortida = fitxerSortida;
            this.iniciMs = iniciMs;
        }
    }
}
