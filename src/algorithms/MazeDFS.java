package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Génération d'un arbre couvrant par parcours en profondeur aléatoire avec retour arrière
 * (« randomized DFS »). Le résultat est un arbre couvrant valide, mais pas un arbre couvrant
 * minimal : les poids des arêtes sont ignorés.
 *
 * <p>La pile est gérée explicitement (tableau d'entiers) pour éviter un débordement de pile
 * d'appels sur de grandes grilles. Une même graine donne toujours le même arbre.</p>
 *
 * <p>Classe fournie : ne pas modifier.</p>
 */
public final class MazeDFS {

    private MazeDFS() {
    }

    /**
     * Génère un arbre couvrant du graphe à partir du sommet 0.
     *
     * @param graph graphe connexe
     * @param seed  graine du générateur pseudo-aléatoire
     * @return les {@code V - 1} arêtes de l'arbre
     * @throws IllegalArgumentException si le graphe n'est pas connexe
     */
    public static List<Edge> generate(Graph graph, long seed) {
        int n = graph.getVertexCount();
        List<Edge> tree = new ArrayList<>(Math.max(0, n - 1));
        if (n == 0) {
            return tree;
        }

        Random random = new Random(seed);
        boolean[] visited = new boolean[n];
        int[] stack = new int[n];       // chaque sommet est empilé au plus une fois
        int top = 0;
        List<Edge> candidates = new ArrayList<>(4);

        visited[0] = true;
        stack[top++] = 0;

        while (top > 0) {
            int current = stack[top - 1];

            candidates.clear();
            for (Edge e : graph.getNeighbors(current)) {
                if (!visited[e.getDestination()]) {
                    candidates.add(e);
                }
            }

            if (candidates.isEmpty()) {
                top--;                                   // retour arrière
            } else {
                Edge chosen = candidates.get(random.nextInt(candidates.size()));
                int next = chosen.getDestination();
                visited[next] = true;
                tree.add(chosen);
                stack[top++] = next;
            }
        }

        if (tree.size() != n - 1) {
            throw new IllegalArgumentException("Graphe non connexe : " + tree.size()
                    + " arêtes trouvées au lieu de " + (n - 1));
        }
        return tree;
    }
}
