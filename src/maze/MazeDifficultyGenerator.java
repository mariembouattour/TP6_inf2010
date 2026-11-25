package maze;

import algorithms.Kruskal;
import algorithms.MazeDFS;
import algorithms.Prim;
import graph.Edge;
import graph.Graph;
import graph.GridGraphFactory;

import java.util.Arrays;
import java.util.List;

/**
 * BONUS — génération d'un labyrinthe selon un niveau de difficulté.
 *
 * <p>===== CLASSE À COMPLÉTER PAR LES ÉTUDIANTS (bonus seulement) =====</p>
 *
 * <p>Cette classe n'est jamais appelée par le banc d'essai obligatoire ({@code Main}).
 * Pour la tester séparément, utilisez {@code BonusMain}.</p>
 */
public final class MazeDifficultyGenerator {

    /** Nombre maximal de labyrinthes candidats à essayer (garantit que la méthode termine). */
    public static final int MAX_ATTEMPTS = 50;

    private static final double EASY_MAX_RATIO = 1.3;
    private static final double MEDIUM_MIN_RATIO = 1.6;
    private static final double MEDIUM_MAX_RATIO = 2.5;
    private static final double HARD_MIN_RATIO = 3.0;

    private MazeDifficultyGenerator() {
    }

    /**
     * Génère un labyrinthe (parfait) correspondant au niveau de difficulté demandé.
     *
     * <p>Exigences :</p>
     * <ul>
     *   <li>choisir et <b>justifier dans votre rapport</b> vos propres métriques de difficulté
     *       (longueur du chemin entrée-sortie, nombre d'impasses, changements de direction, etc.) ;</li>
     *   <li>réutiliser {@code Prim}, {@code Kruskal} et {@code MazeDFS} au lieu de les réécrire ;</li>
     *   <li>essayer au plus {@link #MAX_ATTEMPTS} candidats (par exemple en variant la graine
     *       ou en regénérant les poids avec {@code GridGraphFactory.generate(graph.getRows(), graph.getCols(), nouvelleGraine)}) ;</li>
     *   <li>si le niveau demandé n'est pas atteint après {@link #MAX_ATTEMPTS} tentatives, adopter un
     *       comportement documenté ci-dessous (retourner le meilleur candidat, ou lever une exception).</li>
     * </ul>
     *
     * <p>Convention de la classe {@code Maze} : l'entrée est la cellule 0 (en haut à gauche) et la sortie
     * est la dernière cellule (en bas à droite), voir {@code Maze.getEntrance()} et {@code Maze.getExit()}.</p>
     *
     * <p>Les dimensions de la grille sont accessibles par {@code graph.getRows()} et
     * {@code graph.getCols()} ; on convertit une liste d'arêtes en labyrinthe avec
     * {@code Maze.fromEdges(rows, cols, edges)}.</p>
     *
     * <p>TODO : documenter ici le comportement choisi lorsque le niveau n'est pas atteignable.</p>
     *
     * @param graph      graphe de grille connexe (construit par {@code GridGraphFactory})
     * @param difficulty niveau demandé
     * @param seed       graine (le résultat doit être reproductible)
     * @return un labyrinthe parfait
     */
    public static Maze generate(Graph graph, Difficulty difficulty, long seed) {
        // TODO (bonus) : implanter la génération selon la difficulté.
        if (graph == null || !graph.isGrid()) {
            throw new IllegalArgumentException("Le graphe doit provenir d'une grille.");
        }
        int rows = graph.getRows();
        int cols = graph.getCols();

        Maze best = null;
        double bestDistance = Double.POSITIVE_INFINITY;

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            List<Edge> edges = generateCandidate(graph, difficulty, seed, attempt);
            Maze candidate = Maze.fromEdges(rows, cols, edges);
            double distance = distanceToTarget(pathRatio(candidate), difficulty);
            if (distance == 0) {
                return candidate;
            }
            if (distance < bestDistance) {
                bestDistance = distance;
                best = candidate;
            }
        }
        return best;
    }

    /** Produit les arêtes d'un candidat selon le niveau demandé et le numéro de tentative. */
    private static List<Edge> generateCandidate(Graph graph, Difficulty difficulty, long seed, int attempt) {
        long candidateSeed = seed + attempt;
        switch (difficulty) {
            case EASY:
                return Kruskal.computeMST(weightedGraph(graph, candidateSeed, attempt));
            case MEDIUM:
                if (attempt % 2 == 0) {
                    return Prim.computeMST(weightedGraph(graph, candidateSeed, attempt));
                }
                return MazeDFS.generate(graph, candidateSeed);
            case HARD:
                return MazeDFS.generate(graph, candidateSeed);
            default:
                throw new IllegalArgumentException("Difficulté inconnue : " + difficulty);
        }
    }

    /** Graphe reçu pour la première tentative, puis la même grille avec des poids régénérés. */
    private static Graph weightedGraph(Graph graph, long candidateSeed, int attempt) {
        if (attempt == 0) {
            return graph;
        }
        return GridGraphFactory.generate(graph.getRows(), graph.getCols(), candidateSeed);
    }

    /** Distance entre le ratio et l'intervalle du niveau (0 si le ratio est dans l'intervalle). */
    private static double distanceToTarget(double ratio, Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return Math.max(0, ratio - EASY_MAX_RATIO);
            case MEDIUM:
                return Math.max(0, Math.max(MEDIUM_MIN_RATIO - ratio, ratio - MEDIUM_MAX_RATIO));
            case HARD:
                return Math.max(0, HARD_MIN_RATIO - ratio);
            default:
                throw new IllegalArgumentException("Difficulté inconnue : " + difficulty);
        }
    }

    /** Ratio R = longueur du chemin entrée-sortie (en cellules) / (rows + cols - 1). */
    private static double pathRatio(Maze maze) {
        int minimalLength = maze.getRows() + maze.getCols() - 1;
        return (double) pathLength(maze) / minimalLength;
    }

    /** Nombre de cellules du chemin unique entre l'entrée et la sortie (parcours en largeur). */
    private static int pathLength(Maze maze) {
        int rows = maze.getRows();
        int cols = maze.getCols();
        int cellCount = rows * cols;
        int[] distance = new int[cellCount];
        Arrays.fill(distance, -1);
        int[] queue = new int[cellCount];
        int head = 0;
        int tail = 0;
        queue[tail++] = maze.getEntrance();
        distance[maze.getEntrance()] = 1;

        while (head < tail) {
            int cell = queue[head++];
            if (cell == maze.getExit()) {
                return distance[cell];
            }
            int row = cell / cols;
            int col = cell % cols;
            int[] neighbors = {
                    col + 1 < cols ? cell + 1 : -1,
                    col > 0 ? cell - 1 : -1,
                    row + 1 < rows ? cell + cols : -1,
                    row > 0 ? cell - cols : -1
            };
            for (int next : neighbors) {
                if (next >= 0 && distance[next] == -1 && maze.isPassageOpen(cell, next)) {
                    distance[next] = distance[cell] + 1;
                    queue[tail++] = next;
                }
            }
        }
        throw new IllegalStateException("La sortie n'est pas accessible : le labyrinthe n'est pas parfait.");
    }
}
