package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Algorithme de Kruskal avec {@link FastUnionFind}.
 *
 * <p>===== CLASSE À COMPLÉTER PAR LES ÉTUDIANTS =====</p>
 */
public final class Kruskal {

    private Kruskal() {
    }

    /**
     * Calcule un arbre couvrant minimal du graphe avec l'algorithme de Kruskal.
     *
     * <p>Contraintes :</p>
     * <ul>
     *   <li>trier les arêtes par poids croissant et utiliser {@link FastUnionFind} ;</li>
     *   <li>{@code graph.getEdges()} est non modifiable : trier une <b>copie</b>
     *       ({@code new ArrayList<>(graph.getEdges())}) ;</li>
     *   <li>s'arrêter dès que {@code V - 1} arêtes ont été sélectionnées ;</li>
     *   <li>retourner exactement {@code V - 1} arêtes si le graphe est connexe ;</li>
     *   <li>sinon, lever une {@link IllegalArgumentException} avec un message clair.</li>
     * </ul>
     *
     * @param graph graphe non orienté et pondéré
     * @return les arêtes de l'arbre couvrant minimal
     */
    public static List<Edge> computeMST(Graph graph) {
        int n = graph.getVertexCount();
        List<Edge> tree = new ArrayList<>();
        if (n == 0) {
            return tree;
        }

        List<Edge> edges = new ArrayList<>(graph.getEdges());
        Collections.sort(edges);

        FastUnionFind uf = new FastUnionFind(n);
        for (Edge e : edges) {
            int u = e.getSource();
            int v = e.getDestination();
            if (!uf.connected(u, v)) {
                uf.union(u, v);
                tree.add(e);
                if (tree.size() == n - 1) {
                    break;
                }
            }
        }

        if (tree.size() != n - 1) {
            throw new IllegalArgumentException("Graphe non connexe : seulement "
                    + tree.size() + " arêtes trouvées au lieu de " + (n - 1) + ".");
        }
        return tree;
    }
}