package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.List;

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
        // TODO : implanter l'algorithme de Prim.
        //   1. choisir un sommet de départ et le marquer comme visité ;
        //   2. ajouter ses arêtes dans la file de priorité ;
        //   3. retirer l'arête de poids minimal ; si elle mène à un sommet déjà visité, l'ignorer ;
        //   4. sinon l'ajouter à l'arbre, visiter le nouveau sommet et ajouter ses arêtes candidates ;
        //   5. répéter jusqu'à V - 1 arêtes ; signaler clairement un graphe non connexe.
        throw new UnsupportedOperationException("TODO : Prim.computeMST() n'est pas encore implantée.");
    }
}
