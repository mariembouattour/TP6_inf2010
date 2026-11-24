package graph;

import java.util.Random;

/**
 * Génère le graphe d'une grille de cellules.
 *
 * <p>Convention : la cellule (row, col) a pour identifiant {@code row * cols + col}.
 * Deux cellules adjacentes horizontalement ou verticalement sont reliées par une arête.
 * Les poids sont des entiers pseudo-aléatoires dans [{@value #MIN_WEIGHT}, {@value #MAX_WEIGHT}],
 * tirés dans un ordre fixe (de gauche à droite, de haut en bas, arête de droite puis arête du bas)
 * à partir de la graine : une même graine donne toujours exactement le même graphe.</p>
 *
 * <p>Classe fournie : ne pas modifier.</p>
 */
public final class GridGraphFactory {

    public static final int MIN_WEIGHT = 1;
    public static final int MAX_WEIGHT = 1000;

    private GridGraphFactory() {
    }

    /** Génère une grille {@code rows x cols} avec des poids aléatoires issus de {@code seed}. */
    public static Graph generate(int rows, int cols, long seed) {
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException("Dimensions invalides : " + rows + "x" + cols);
        }
        int vertexCount = Math.multiplyExact(rows, cols);
        Graph graph = new Graph(vertexCount, rows, cols);
        Random random = new Random(seed);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int id = vertexId(r, c, cols);
                if (c + 1 < cols) {
                    graph.addEdge(id, id + 1, randomWeight(random));
                }
                if (r + 1 < rows) {
                    graph.addEdge(id, id + cols, randomWeight(random));
                }
            }
        }
        return graph;
    }

    /** Identifiant du sommet correspondant à la cellule (row, col). */
    public static int vertexId(int row, int col, int cols) {
        return row * cols + col;
    }

    /** Ligne de la cellule {@code vertexId}. */
    public static int rowOf(int vertexId, int cols) {
        return vertexId / cols;
    }

    /** Colonne de la cellule {@code vertexId}. */
    public static int colOf(int vertexId, int cols) {
        return vertexId % cols;
    }

    private static int randomWeight(Random random) {
        return MIN_WEIGHT + random.nextInt(MAX_WEIGHT - MIN_WEIGHT + 1);
    }
}
