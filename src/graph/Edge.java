package graph;

/**
 * Arête d'un graphe non orienté et pondéré.
 *
 * <p>L'arête (a, b, w) et l'arête (b, a, w) sont considérées comme identiques
 * (méthodes {@code equals} et {@code hashCode}). L'ordre naturel ({@code compareTo})
 * trie par poids croissant ; en cas d'égalité de poids, il départage selon les
 * extrémités afin que le tri soit toujours déterministe.</p>
 *
 * <p>Classe fournie : ne pas modifier.</p>
 */
public class Edge implements Comparable<Edge> {

    private final int source;
    private final int destination;
    private final int weight;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    /** Tri par poids croissant, puis par plus petite extrémité, puis par plus grande. */
    @Override
    public int compareTo(Edge other) {
        int cmp = Integer.compare(weight, other.weight);
        if (cmp != 0) {
            return cmp;
        }
        cmp = Integer.compare(Math.min(source, destination), Math.min(other.source, other.destination));
        if (cmp != 0) {
            return cmp;
        }
        return Integer.compare(Math.max(source, destination), Math.max(other.source, other.destination));
    }

    /** Deux arêtes sont égales si elles ont le même poids et les mêmes extrémités, peu importe l'ordre. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edge)) {
            return false;
        }
        return compareTo((Edge) o) == 0;
    }

    @Override
    public int hashCode() {
        int low = Math.min(source, destination);
        int high = Math.max(source, destination);
        return 31 * (31 * low + high) + weight;
    }

    @Override
    public String toString() {
        return "(" + source + " - " + destination + ", poids=" + weight + ")";
    }
}
