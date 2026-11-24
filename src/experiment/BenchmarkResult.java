package experiment;

/**
 * Résultat d'une série d'expériences pour une taille de grille.
 * Les temps sont des médianes exprimées en millisecondes.
 *
 * <p>Classe fournie : ne pas modifier.</p>
 */
public class BenchmarkResult {

    private final int rows;
    private final int cols;
    private final int vertexCount;
    private final int edgeCount;
    private final double primMedianMs;
    private final double kruskalMedianMs;
    private final double dfsMedianMs;

    public BenchmarkResult(int rows, int cols, int vertexCount, int edgeCount,
                           double primMedianMs, double kruskalMedianMs, double dfsMedianMs) {
        this.rows = rows;
        this.cols = cols;
        this.vertexCount = vertexCount;
        this.edgeCount = edgeCount;
        this.primMedianMs = primMedianMs;
        this.kruskalMedianMs = kruskalMedianMs;
        this.dfsMedianMs = dfsMedianMs;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public double getPrimMedianMs() {
        return primMedianMs;
    }

    public double getKruskalMedianMs() {
        return kruskalMedianMs;
    }

    public double getDfsMedianMs() {
        return dfsMedianMs;
    }
}
