package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Graphe non orienté et pondéré.
 *
 * <p>Représentation :</p>
 * <ul>
 *   <li>{@code adjacencyList.get(v)} contient les arêtes qui partent de {@code v}.
 *       Chaque arête {u, v} y apparaît deux fois : une fois dans la liste de {@code u}
 *       (sous la forme u→v) et une fois dans la liste de {@code v} (sous la forme v→u).
 *       Pour toute arête {@code e} de {@code getNeighbors(v)}, on a donc
 *       {@code e.getSource() == v} et {@code e.getDestination()} est le voisin.</li>
 *   <li>{@code edges} contient chaque arête une seule fois (utile pour Kruskal).</li>
 * </ul>
 *
 * <p>Les listes retournées par {@link #getNeighbors(int)} et {@link #getEdges()} sont
 * <b>non modifiables</b> : un algorithme ne peut pas altérer le graphe d'entrée.
 * Si vous devez trier les arêtes, travaillez sur une copie
 * ({@code new ArrayList<>(graph.getEdges())}).</p>
 *
 * <p>Un graphe construit par {@link GridGraphFactory} mémorise aussi les dimensions de
 * sa grille ({@link #getRows()}, {@link #getCols()}), ce qui permet de convertir
 * un arbre couvrant en {@code Maze}.</p>
 *
 * <p>Classe fournie : ne pas modifier.</p>
 */
public class Graph {

    private final int vertexCount;
    private final int rows;
    private final int cols;
    private final List<List<Edge>> adjacencyList;
    private final List<Edge> edges;

    /** Graphe général sans structure de grille. */
    public Graph(int vertexCount) {
        this(vertexCount, -1, -1);
    }

    /** Graphe issu d'une grille {@code rows x cols} (voir {@link GridGraphFactory}). */
    public Graph(int vertexCount, int rows, int cols) {
        if (vertexCount < 0) {
            throw new IllegalArgumentException("Nombre de sommets négatif : " + vertexCount);
        }
        if (rows > 0 && (long) rows * cols != vertexCount) {
            throw new IllegalArgumentException("rows * cols doit être égal au nombre de sommets");
        }
        this.vertexCount = vertexCount;
        this.rows = rows;
        this.cols = cols;
        this.adjacencyList = new ArrayList<>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            adjacencyList.add(new ArrayList<>(4));
        }
        this.edges = new ArrayList<>();
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    /** Arêtes sortantes de {@code vertex} (liste non modifiable). */
    public List<Edge> getNeighbors(int vertex) {
        checkVertex(vertex);
        return Collections.unmodifiableList(adjacencyList.get(vertex));
    }

    /** Toutes les arêtes du graphe, chacune une seule fois (liste non modifiable). */
    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    /** Ajoute l'arête non orientée {source, destination} de poids {@code weight}. */
    public void addEdge(int source, int destination, int weight) {
        checkVertex(source);
        checkVertex(destination);
        if (source == destination) {
            throw new IllegalArgumentException("Boucle interdite sur le sommet " + source);
        }
        Edge forward = new Edge(source, destination, weight);
        edges.add(forward);
        adjacencyList.get(source).add(forward);
        adjacencyList.get(destination).add(new Edge(destination, source, weight));
    }

    /** Retourne l'arête {u, v} vue depuis {@code u}, ou {@code null} si elle n'existe pas. */
    public Edge findEdge(int u, int v) {
        checkVertex(u);
        checkVertex(v);
        for (Edge e : adjacencyList.get(u)) {
            if (e.getDestination() == v) {
                return e;
            }
        }
        return null;
    }

    /** {@code true} si le graphe provient d'une grille. */
    public boolean isGrid() {
        return rows > 0;
    }

    /** Nombre de lignes de la grille (ou -1 si ce n'est pas un graphe de grille). */
    public int getRows() {
        return rows;
    }

    /** Nombre de colonnes de la grille (ou -1 si ce n'est pas un graphe de grille). */
    public int getCols() {
        return cols;
    }

    private void checkVertex(int v) {
        if (v < 0 || v >= vertexCount) {
            throw new IndexOutOfBoundsException("Sommet invalide : " + v + " (0.." + (vertexCount - 1) + ")");
        }
    }
}
