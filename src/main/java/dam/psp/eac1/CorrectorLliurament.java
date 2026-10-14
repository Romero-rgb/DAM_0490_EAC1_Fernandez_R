package dam.psp.eac1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CorrectorLliurament {
    public boolean validar(Path ruta) {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta del lliurament no pot ser null.");
        }

        try {
            Path fitxerLliurament = obtenirRutaFitxerLliurament(ruta);
            String contingut = llegirContingut(fitxerLliurament);
            return esLliuramentCorrecte(contingut);
        } catch (IllegalArgumentException | IOException e) {
            return false;
        }
    }

    public Path obtenirRutaFitxerLliurament(Path ruta) {

        if (ruta == null) {
            throw new IllegalArgumentException("La ruta del lliurament no pot ser null");
        }

        if (Files.isDirectory(ruta)) {
            return ruta.resolve("lliurament.txt");
        }else if (Files.isRegularFile(ruta)) {
            return ruta;
        }else {
            throw new IllegalArgumentException("La ruta del lliurament ha de ser un fitxer o un directori");
        }
    }

    public String llegirContingut(Path fitxer) throws IOException {

        if ( fitxer == null || !Files.isRegularFile(fitxer) || !Files.isReadable(fitxer) ) {
            throw new IOException("El fitxer del lliurament es null o no es regular");
        }
        String contingut = Files.readString(fitxer);
        return contingut.toLowerCase().trim();

    }

    public boolean esLliuramentCorrecte(String contingut) {

        if (contingut == null) {
            throw new IllegalArgumentException("El fitxer és buit");
        }

        return contingut.equals("correcte");

    }
}
