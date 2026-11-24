package algorithms;

import graph.Edge;
import graph.Graph;

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
        // TODO : implanter l'algorithme de Kruskal.
        //   1. copier et trier les arêtes par poids croissant ;
        //   2. créer un FastUnionFind sur les sommets ;
        //   3. pour chaque arête : si ses extrémités sont dans deux composantes distinctes,
        //      la retenir et fusionner les composantes ;
        //   4. arrêter à V - 1 arêtes ; signaler clairement un graphe non connexe.
        throw new UnsupportedOperationException("TODO : Kruskal.computeMST() n'est pas encore implantée.");
    }
}
