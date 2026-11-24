package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Algorithme de Prim avec liste d'adjacence et file de priorité (monceau binaire).
 *
 * <p>===== CLASSE À COMPLÉTER PAR LES ÉTUDIANTS =====</p>
 */
public final class Prim {

    private Prim() {
    }

    /**
     * Calcule un arbre couvrant minimal du graphe avec l'algorithme de Prim.
     *
     * <p>Contraintes :</p>
     * <ul>
     *   <li>utiliser la liste d'adjacence ({@code graph.getNeighbors(v)}) et
     *       {@link java.util.PriorityQueue} (les arêtes se comparent par poids grâce à {@code compareTo}) ;</li>
     *   <li>ne pas modifier le graphe (ses listes sont de toute façon non modifiables) ;</li>
     *   <li>retourner exactement {@code V - 1} arêtes si le graphe est connexe ;</li>
     *   <li>si le graphe n'est pas connexe, lever une {@link IllegalArgumentException}
     *       avec un message clair (ne jamais retourner un résultat incomplet).</li>
     * </ul>
     *
     * <p>Rappel : pour chaque arête {@code e} de {@code graph.getNeighbors(v)},
     * {@code e.getSource() == v} et {@code e.getDestination()} est le voisin.</p>
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

        boolean[] visited = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        visit(graph, 0, visited, pq);

        while (tree.size() < n - 1 && !pq.isEmpty()) {
            Edge e = pq.poll();
            int v = e.getDestination();
            if (visited[v]) {
                continue;
            }
            tree.add(e);
            visit(graph, v, visited, pq);
        }

        if (tree.size() != n - 1) {
            throw new IllegalArgumentException("Graphe non connexe : seulement "
                    + (tree.size() + 1) + " sommets atteints sur " + n + ".");
        }
        return tree;
    }

    private static void visit(Graph graph, int v, boolean[] visited, PriorityQueue<Edge> pq) {
        visited[v] = true;
        for (Edge e : graph.getNeighbors(v)) {
            if (!visited[e.getDestination()]) {
                pq.add(e);
            }
        }
    }
}