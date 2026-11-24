package maze;

import graph.Edge;

import java.util.List;

/**
 * Labyrinthe sur une grille {@code rows x cols}. La cellule (row, col) a pour identifiant
 * {@code row * cols + col} (même convention que {@code GridGraphFactory}).
 *
 * <p>Convention : l'<b>entrée</b> est la cellule en haut à gauche (identifiant 0, voir
 * {@link #getEntrance()}) et la <b>sortie</b> est la cellule en bas à droite (identifiant
 * {@code rows * cols - 1}, voir {@link #getExit()}). L'entrée et la sortie ne dépendent pas des
 * passages ouverts. Elles sont marquées {@code E} et {@code S} par {@link #display()}.</p>
 *
 * <p>Un passage ouvert entre deux cellules voisines correspond à une arête de l'arbre couvrant.
 * Toutes les autres frontières entre cellules sont des murs.</p>
 *
 * <p>Classe fournie : ne pas modifier.</p>
 */
public class Maze {

    private final int rows;
    private final int cols;
    /** openRight[r][c] : passage ouvert entre (r, c) et (r, c + 1). */
    private final boolean[][] openRight;
    /** openDown[r][c] : passage ouvert entre (r, c) et (r + 1, c). */
    private final boolean[][] openDown;
    private int openPassageCount;

    private Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.openRight = new boolean[rows][cols];
        this.openDown = new boolean[rows][cols];
    }

    /**
     * Construit un labyrinthe en ouvrant un passage pour chaque arête de la liste.
     *
     * @throws IllegalArgumentException si une arête ne relie pas deux cellules voisines
     *                                  ou si un même passage est ouvert deux fois
     */
    public static Maze fromEdges(int rows, int cols, List<Edge> edges) {
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException("Dimensions invalides : " + rows + "x" + cols);
        }
        Maze maze = new Maze(rows, cols);
        int cellCount = rows * cols;
        for (Edge e : edges) {
            int a = e.getSource();
            int b = e.getDestination();
            if (a < 0 || a >= cellCount || b < 0 || b >= cellCount) {
                throw new IllegalArgumentException("Arête hors de la grille : " + e);
            }
            maze.openPassage(a, b);
        }
        return maze;
    }

    private void openPassage(int a, int b) {
        int low = Math.min(a, b);
        int high = Math.max(a, b);
        int r = low / cols;
        int c = low % cols;
        boolean[][] target;
        if (high == low + 1 && c + 1 < cols) {
            target = openRight;
        } else if (high == low + cols) {
            target = openDown;
        } else {
            throw new IllegalArgumentException("Les cellules " + a + " et " + b + " ne sont pas voisines");
        }
        if (target[r][c]) {
            throw new IllegalArgumentException("Passage déjà ouvert entre " + a + " et " + b);
        }
        target[r][c] = true;
        openPassageCount++;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /** Identifiant de la cellule d'entrée (en haut à gauche) : toujours 0. */
    public int getEntrance() {
        return 0;
    }

    /** Identifiant de la cellule de sortie (en bas à droite) : {@code rows * cols - 1}. */
    public int getExit() {
        return rows * cols - 1;
    }

    /** Nombre de passages ouverts. */
    public int getOpenPassageCount() {
        return openPassageCount;
    }

    /** Retourne {@code true} si les deux cellules sont voisines et reliées par un passage ouvert. */
    public boolean isPassageOpen(int cell1, int cell2) {
        int cellCount = rows * cols;
        if (cell1 < 0 || cell1 >= cellCount || cell2 < 0 || cell2 >= cellCount) {
            throw new IllegalArgumentException("Cellule invalide : " + cell1 + " ou " + cell2);
        }
        int low = Math.min(cell1, cell2);
        int high = Math.max(cell1, cell2);
        int r = low / cols;
        int c = low % cols;
        if (high == low + 1 && c + 1 < cols) {
            return openRight[r][c];
        }
        if (high == low + cols) {
            return openDown[r][c];
        }
        return false;
    }

    /**
     * Vérifie que le labyrinthe est parfait : toutes les cellules sont accessibles depuis la
     * cellule 0 et il n'y a aucun cycle. Un graphe connexe de V sommets est sans cycle
     * si et seulement s'il a exactement V - 1 arêtes : on vérifie donc la connexité par un
     * parcours en largeur, puis le nombre de passages ouverts.
     */
    public boolean isPerfectMaze() {
        int cellCount = rows * cols;
        boolean[] seen = new boolean[cellCount];
        int[] queue = new int[cellCount];
        int head = 0;
        int tail = 0;
        queue[tail++] = 0;
        seen[0] = true;
        while (head < tail) {
            int cell = queue[head++];
            int r = cell / cols;
            int c = cell % cols;
            if (c + 1 < cols && openRight[r][c] && !seen[cell + 1]) {
                seen[cell + 1] = true;
                queue[tail++] = cell + 1;
            }
            if (c > 0 && openRight[r][c - 1] && !seen[cell - 1]) {
                seen[cell - 1] = true;
                queue[tail++] = cell - 1;
            }
            if (r + 1 < rows && openDown[r][c] && !seen[cell + cols]) {
                seen[cell + cols] = true;
                queue[tail++] = cell + cols;
            }
            if (r > 0 && openDown[r - 1][c] && !seen[cell - cols]) {
                seen[cell - cols] = true;
                queue[tail++] = cell - cols;
            }
        }
        return tail == cellCount && openPassageCount == cellCount - 1;
    }

    /** Représentation en texte du labyrinthe ('+', '-' et '|' pour les murs, 'E' entrée, 'S' sortie). */
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        appendTopBorder(sb);
        for (int r = 0; r < rows; r++) {
            sb.append('|');
            for (int c = 0; c < cols; c++) {
                int cell = r * cols + c;
                sb.append(cell == getEntrance() ? " E " : cell == getExit() ? " S " : "   ");
                sb.append(c + 1 < cols && openRight[r][c] ? ' ' : '|');
            }
            sb.append('\n');
            for (int c = 0; c < cols; c++) {
                sb.append('+');
                sb.append(r + 1 < rows && openDown[r][c] ? "   " : "---");
            }
            sb.append("+\n");
        }
        return sb.toString();
    }

    /** Affiche le labyrinthe dans la console (à réserver aux petites grilles). */
    public void display() {
        System.out.print(toDisplayString());
    }

    private void appendTopBorder(StringBuilder sb) {
        for (int c = 0; c < cols; c++) {
            sb.append("+---");
        }
        sb.append("+\n");
    }
}
