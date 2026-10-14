package dam.psp.eac1;

import java.nio.file.Path;

public class ValidadorLliurament {
    public static final int CODI_CORRECTE = 0;
    public static final int CODI_ERROR_VALIDACIO = 2;
    public static final int CODI_ARGUMENTS_INVALIDS = 10;

    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[0].isBlank() || args[1] == null || args[1].isBlank()) {
            System.err.println("Us: java dam.psp.eac1.ValidadorLliurament <alumneId> <rutaLliurament>");
            System.exit(CODI_ARGUMENTS_INVALIDS);
        }

        String alumneId = args[0];
        Path ruta = Path.of(args[1]);

        int codi = validar(alumneId, ruta);
        System.exit(codi);
    }

    public static int validar(String alumneId, Path ruta) {
        if (alumneId == null || alumneId.isBlank()) {
            throw new IllegalArgumentException("L'identificador de l'alumne no pot ser buit.");
        }
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta del lliurament no pot ser null.");
        }

        dormir(500L);

        CorrectorLliurament corrector = new CorrectorLliurament();
        boolean correcte = corrector.validar(ruta);

        System.out.println("ALUMNE=" + alumneId);
        System.out.println("RUTA=" + ruta);
        System.out.println("RESULTAT=" + (correcte ? "CORRECTE" : "ERROR"));

        return correcte ? CODI_CORRECTE : CODI_ERROR_VALIDACIO;
    }

    private static void dormir(long tempsMs) {
        try {
            Thread.sleep(Math.max(0L, tempsMs));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
