package dam.psp.eac1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public final class CarregadorLliuraments {
    public static final Path DIRECTORI_LLIURAMENTS = Path.of("lliuraments");

    private CarregadorLliuraments() {
    }

    public static List<Lliurament> carregar() throws IOException {
        return carregar(DIRECTORI_LLIURAMENTS);
    }

    public static List<Lliurament> carregar(Path directoriLliuraments) throws IOException {
        if (directoriLliuraments == null) {
            throw new IllegalArgumentException("El directori de lliuraments no pot ser null.");
        }
        if (!Files.isDirectory(directoriLliuraments)) {
            throw new IOException("El directori de lliuraments no existeix o no és un directori: " + directoriLliuraments);
        }

        AtomicInteger posicio = new AtomicInteger(1);

        try (Stream<Path> entrades = Files.list(directoriLliuraments)) {
            return entrades
                    .filter(Files::isDirectory)
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .map(ruta -> crearLliurament(ruta, posicio.getAndIncrement()))
                    .toList();
        }
    }

    private static Lliurament crearLliurament(Path ruta, int posicio) {
        String alumneId = ruta.getFileName().toString();
        long variacioMs = Math.floorMod((alumneId.hashCode() * 31L) + (posicio * 97L), 450L);
        long tempsSimulatMs = 250L + variacioMs;
        return new Lliurament(alumneId, ruta, tempsSimulatMs);
    }
}
