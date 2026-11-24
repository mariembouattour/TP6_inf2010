import experiment.Benchmark;

import java.nio.file.Paths;

/**
 * Point d'entrée : lance les expérimentations et génère le fichier CSV.
 * À lancer depuis le dossier TP6 (voir README.md).
 *
 * <p>Fichier fourni : ne pas modifier.</p>
 */
public class Main {

    public static void main(String[] args) {
        try {
            Benchmark.runExperiments();
        } catch (IllegalStateException e) {
            System.err.println();
            System.err.println("EXPÉRIENCE INTERROMPUE : " + e.getMessage());
            System.err.println("Aucun résultat n'a été enregistré.");
            System.exit(1);
        }
        System.out.println();
        System.out.println("Résultats enregistrés dans : "
                + Paths.get(Benchmark.OUTPUT_FILE).toAbsolutePath());
    }
}
