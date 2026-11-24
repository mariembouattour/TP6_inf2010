package experiment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/**
 * Exporte les résultats du banc d'essai au format CSV.
 *
 * <p>Colonnes :</p>
 * <ul>
 *   <li>{@code rows}, {@code cols} : dimensions de la grille ;</li>
 *   <li>{@code V} : nombre de sommets (rows * cols) ;</li>
 *   <li>{@code E} : nombre d'arêtes du graphe de la grille ;</li>
 *   <li>{@code prim_median_ms}, {@code kruskal_median_ms}, {@code dfs_median_ms} :
 *       temps d'exécution médian (millisecondes) de chaque algorithme.</li>
 * </ul>
 * <p>Le séparateur décimal est toujours le point, quelle que soit la langue du système.
 * Le dossier de sortie est créé au besoin et un fichier existant est remplacé.</p>
 *
 * <p>Classe fournie : ne pas modifier.</p>
 */
public final class CSVWriter {

    public static final String HEADER = "rows,cols,V,E,prim_median_ms,kruskal_median_ms,dfs_median_ms";

    private CSVWriter() {
    }

    public static void writeResults(List<BenchmarkResult> results, String filename) {
        Path path = Paths.get(filename);
        try {
            Path parent = path.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                out.write(HEADER);
                out.write("\n");
                for (BenchmarkResult r : results) {
                    out.write(String.format(Locale.ROOT, "%d,%d,%d,%d,%.6f,%.6f,%.6f%n",
                            r.getRows(), r.getCols(), r.getVertexCount(), r.getEdgeCount(),
                            r.getPrimMedianMs(), r.getKruskalMedianMs(), r.getDfsMedianMs()));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible d'écrire " + filename, e);
        }
    }
}
