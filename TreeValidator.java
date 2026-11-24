package experiment;

import graph.Edge;
import graph.Graph;
import maze.Maze;

import java.util.List;

/**
 * Vérifie qu'une liste d'arêtes est bien un arbre couvrant d'un graphe de grille
 * et que le labyrinthe correspondant est parfait.
 *
 * <p>Ce validateur utilise sa propre structure Union-Find (il ne dépend pas de
 * {@code FastUnionFind}, qui est écrite par les étudiants).</p>
 *
 * <p>Classe fournie : ne pas modifier.</p>
 */
public final class TreeValidator {

    private TreeValidator() {
    }

    /**
     * Valide l'arbre et retourne son poids total.
     *
     * @param label nom de l'algorithme (pour les messages d'erreur)
     * @throws IllegalStateException si l'arbre est invalide
     */
    public static long validate(Graph graph, List<Edge> tree, String label) {
        if (tree == null) {
            throw error(label, "le résultat est null");
        }
        int n = graph.getVertexCount();
        if (tree.size() != n - 1) {
            throw error(label, tree.size() + " arêtes retournées au lieu de V - 1 = " + (n - 1));
        }

        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        long totalWeight = 0;
        for (Edge e : tree) {
            int u = e.getSource();
            int v = e.getDestination();
            if (u < 0 || u >= n || v < 0 || v >= n) {
                throw error(label, "arête avec un sommet invalide : " + e);
            }
            Edge original = graph.findEdge(u, v);
            if (original == null) {
                throw error(label, "l'arête " + e + " n'existe pas dans le graphe");
            }
            if (original.getWeight() != e.getWeight()) {
                throw error(label, "poids incorrect pour l'arête " + e
                        + " (le graphe contient le poids " + original.getWeight() + ")");
            }
            int ru = find(parent, u);
            int rv = find(parent, v);
            if (ru == rv) {
                throw error(label, "cycle détecté : l'arête " + e + " relie deux sommets déjà connectés");
            }
            parent[ru] = rv;
            totalWeight += e.getWeight();
        }

        int root = find(parent, 0);
        for (int i = 1; i < n; i++) {
            if (find(parent, i) != root) {
                throw error(label, "le sommet " + i + " n'est pas accessible (arbre non couvrant)");
            }
        }

        if (!graph.isGrid()) {
            throw error(label, "le graphe n'est pas une grille : impossible de construire un labyrinthe");
        }
        Maze maze = Maze.fromEdges(graph.getRows(), graph.getCols(), tree);
        if (!maze.isPerfectMaze()) {
            throw error(label, "le labyrinthe obtenu n'est pas parfait");
        }
        return totalWeight;
    }

    private static int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    private static IllegalStateException error(String label, String message) {
        return new IllegalStateException("Résultat invalide pour " + label + " : " + message);
    }
}
