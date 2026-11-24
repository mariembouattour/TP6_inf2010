package experiment;

import algorithms.Kruskal;
import algorithms.MazeDFS;
import algorithms.Prim;
import graph.Edge;
import graph.Graph;
import graph.GridGraphFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Banc d'essai automatisé : Prim, Kruskal et DFS sur des grilles de tailles croissantes.
 *
 * <p>Pour chaque taille :</p>
 * <ol>
 *   <li>(une seule fois, au début : chauffe globale du JIT sur une grille 100x100) ;</li>
 *   <li>génération de la grille et des poids (hors chronométrage, avec une graine déterministe) ;</li>
 *   <li>{@link #WARMUP_RUNS} exécutions de chauffe par algorithme (JIT), non retenues ;</li>
 *   <li>{@link #MEASURED_RUNS} exécutions mesurées avec {@code System.nanoTime()} ;
 *       l'ordre des trois algorithmes change à chaque répétition (rotation circulaire) pour
 *       répartir équitablement les effets de cache et de ramasse-miettes ;</li>
 *   <li>validation de <b>chaque</b> résultat (arbre couvrant, absence de cycle, labyrinthe parfait),
 *       toujours en dehors de la section chronométrée ; de plus, Prim et Kruskal doivent produire
 *       des arbres de même poids total, et le DFS ne peut pas être plus léger ;</li>
 *   <li>médiane des temps, puis écriture du CSV.</li>
 * </ol>
 *
 * <p>Les trois algorithmes reçoivent exactement le même objet {@code Graph}, que ses listes
 * non modifiables protègent contre toute modification.</p>
 *
 * <p><b>Mémoire :</b> aucune estimation de mémoire n'est exportée. Une estimation théorique de la
 * mémoire auxiliaire dépend du détail de l'implantation (taille réelle de la file de priorité,
 * copie des arêtes, etc.) et serait artificielle ; la complexité spatiale est à analyser
 * théoriquement dans le rapport.</p>
 *
 * <p>Si une validation échoue ou si une fonction obligatoire n'est pas implantée, l'expérience est
 * interrompue par une {@link IllegalStateException} et <b>aucun CSV n'est écrit</b>.</p>
 *
 * <p>Classe fournie : ne pas modifier.</p>
 */
public final class Benchmark {

    // ======================= PARAMÈTRES (un seul endroit) =======================

    /** Dimensions {lignes, colonnes} des grilles testées. */
    private static final int[][] DIMENSIONS = {
            {10, 10}, {20, 20}, {30, 30}, {50, 50}, {75, 75}, {100, 100},
            {150, 150}, {200, 200}, {300, 300}, {400, 400}, {500, 500}, {600, 600}
    };

    /** Exécutions de chauffe (non mesurées) par algorithme et par taille. */
    private static final int WARMUP_RUNS = 3;

    /** Exécutions mesurées par algorithme et par taille (nombre impair conseillé pour la médiane). */
    private static final int MEASURED_RUNS = 11;

    /** Chauffe globale (avant toutes les tailles) : nombre d'exécutions par algorithme sur une grille 100x100. */
    private static final int GLOBAL_WARMUP_RUNS = 30;

    /** Graine de base pour la génération des grilles. */
    private static final long BASE_SEED = 2024L;

    /** Graine du DFS. */
    private static final long DFS_SEED = 7L;

    /** Fichier de sortie (relatif au dossier d'où le programme est lancé). */
    public static final String OUTPUT_FILE = "output/output.csv";

    // ============================================================================

    private static final int PRIM = 0;
    private static final int KRUSKAL = 1;
    private static final int DFS = 2;
    private static final String[] NAMES = {"Prim", "Kruskal", "DFS"};

    /** Algorithme de génération d'arbre couvrant. */
    private interface Algorithm {
        List<Edge> run(Graph graph);
    }

    /** Résultat d'une exécution chronométrée. */
    private static final class Run {
        final List<Edge> tree;
        final double millis;

        Run(List<Edge> tree, double millis) {
            this.tree = tree;
            this.millis = millis;
        }
    }

    private Benchmark() {
    }

    /** Lance toutes les expériences puis écrit {@link #OUTPUT_FILE}. */
    public static void runExperiments() {
        Algorithm[] algorithms = {
                Prim::computeMST,
                Kruskal::computeMST,
                g -> MazeDFS.generate(g, DFS_SEED)
        };

        System.out.println("Banc d'essai : " + WARMUP_RUNS + " exécutions de chauffe + "
                + MEASURED_RUNS + " exécutions mesurées par algorithme et par taille.");

        globalWarmup(algorithms);

        List<BenchmarkResult> results = new ArrayList<>();
        for (int[] dim : DIMENSIONS) {
            results.add(runOneSize(dim[0], dim[1], algorithms));
        }
        CSVWriter.writeResults(results, OUTPUT_FILE);
    }

    /** Chauffe initiale du JIT, pour que les petites grilles ne soient pas mesurées en code non compilé. */
    private static void globalWarmup(Algorithm[] algorithms) {
        Graph graph = GridGraphFactory.generate(100, 100, BASE_SEED);
        for (int i = 0; i < GLOBAL_WARMUP_RUNS; i++) {
            for (int a = 0; a < algorithms.length; a++) {
                Run run = timedRun(a, algorithms, graph);
                TreeValidator.validate(graph, run.tree, NAMES[a] + " (chauffe initiale)");
            }
        }
    }

    private static BenchmarkResult runOneSize(int rows, int cols, Algorithm[] algorithms) {
        // 1-2. Génération du graphe et des poids : HORS chronométrage.
        long seed = BASE_SEED + 1_000_003L * rows + cols;
        Graph graph = GridGraphFactory.generate(rows, cols, seed);
        String context = " (grille " + rows + "x" + cols + ")";

        // Chauffe (JIT) : résultats validés mais temps ignorés.
        for (int i = 0; i < WARMUP_RUNS; i++) {
            for (int a = 0; a < algorithms.length; a++) {
                Run run = timedRun(a, algorithms, graph);
                TreeValidator.validate(graph, run.tree, NAMES[a] + context);
            }
        }

        // Mesures.
        double[][] times = new double[algorithms.length][MEASURED_RUNS];
        long[] weights = new long[algorithms.length];
        for (int rep = 0; rep < MEASURED_RUNS; rep++) {
            for (int k = 0; k < algorithms.length; k++) {
                int a = (rep + k) % algorithms.length;      // rotation de l'ordre d'exécution
                System.gc();                                // hors chronométrage
                Run run = timedRun(a, algorithms, graph);
                times[a][rep] = run.millis;
                // Validation : APRÈS la mesure, jamais dans la section chronométrée.
                weights[a] = TreeValidator.validate(graph, run.tree, NAMES[a] + context);
            }
        }

        // Prim et Kruskal doivent trouver des arbres de même poids : c'est le poids minimal.
        if (weights[PRIM] != weights[KRUSKAL]) {
            throw new IllegalStateException("Prim et Kruskal ne produisent pas des arbres de même poids"
                    + context + " : Prim = " + weights[PRIM] + ", Kruskal = " + weights[KRUSKAL]
                    + ". Au moins un des deux n'est pas minimal.");
        }
        if (weights[DFS] < weights[PRIM]) {
            throw new IllegalStateException("Incohérence" + context + " : l'arbre du DFS (" + weights[DFS]
                    + ") est plus léger que celui de Prim (" + weights[PRIM] + "), donc Prim n'est pas minimal.");
        }

        double primMedian = Statistics.median(times[PRIM]);
        double kruskalMedian = Statistics.median(times[KRUSKAL]);
        double dfsMedian = Statistics.median(times[DFS]);

        System.out.println(String.format(Locale.ROOT,
                "%4dx%-4d V=%-7d E=%-7d Prim=%9.3f ms (sd %.3f)  Kruskal=%9.3f ms (sd %.3f)  DFS=%9.3f ms (sd %.3f)",
                rows, cols, graph.getVertexCount(), graph.getEdgeCount(),
                primMedian, Statistics.standardDeviation(times[PRIM]),
                kruskalMedian, Statistics.standardDeviation(times[KRUSKAL]),
                dfsMedian, Statistics.standardDeviation(times[DFS])));

        return new BenchmarkResult(rows, cols, graph.getVertexCount(), graph.getEdgeCount(),
                primMedian, kruskalMedian, dfsMedian);
    }

    /** Exécute un algorithme et mesure uniquement son temps de calcul. */
    private static Run timedRun(int index, Algorithm[] algorithms, Graph graph) {
        try {
            long start = System.nanoTime();
            List<Edge> tree = algorithms[index].run(graph);
            long end = System.nanoTime();
            return new Run(tree, (end - start) / 1_000_000.0);
        } catch (UnsupportedOperationException e) {
            String message = e.getMessage();
            if (message != null && message.startsWith("TODO")) {
                throw new IllegalStateException(message, e);
            }
            throw new IllegalStateException("UnsupportedOperationException dans " + NAMES[index]
                    + " : vous modifiez probablement une liste du graphe (non modifiable). "
                    + "Travaillez sur une copie : new ArrayList<>(graph.getEdges()).", e);
        }
    }
}
