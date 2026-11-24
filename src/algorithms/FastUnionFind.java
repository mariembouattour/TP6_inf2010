package algorithms;

/**
 * Structure Union-Find (ensembles disjoints) optimisée :
 * compression de chemin + union par rang.
 *
 * <p>===== CLASSE À COMPLÉTER PAR LES ÉTUDIANTS : {@link #find(int)} et {@link #union(int, int)} =====</p>
 * <p>Le constructeur, {@link #connected(int, int)} et la vérification des indices sont fournis.</p>
 */
public class FastUnionFind {

    /** {@code parent[i]} : parent de {@code i} dans la forêt (une racine est son propre parent). */
    private final int[] parent;

    /** {@code rank[i]} : borne supérieure de la hauteur de l'arbre dont {@code i} est la racine. */
    private final int[] rank;

    /** Crée {@code size} ensembles disjoints {0}, {1}, ..., {size - 1}. */
    public FastUnionFind(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Taille négative : " + size);
        }
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Retourne la racine (le représentant) de l'ensemble contenant {@code x}.
     *
     * <p>Doit appliquer la <b>compression de chemin</b> : après l'appel, tous les éléments
     * rencontrés sur le chemin vers la racine doivent pointer directement vers elle.</p>
     */
    public int find(int x) {
        checkIndex(x);
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    /**
     * Fusionne les ensembles contenant {@code x} et {@code y}.
     *
     * <p>Doit appliquer l'<b>union par rang</b> : la racine de plus petit rang est rattachée à celle
     * de plus grand rang ; en cas d'égalité, le rang de la racine choisie augmente de 1.
     * Ne fait rien si {@code x} et {@code y} sont déjà dans le même ensemble.</p>
     */
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) {
            return;
        }
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }

    /** Retourne {@code true} si {@code x} et {@code y} sont dans le même ensemble. (Fournie.) */
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    /** Nombre d'éléments gérés. (Fournie.) */
    public int size() {
        return parent.length;
    }

    /** Lève une exception explicite si {@code x} n'est pas un indice valide. (Fournie.) */
    private void checkIndex(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException("Indice invalide : " + x + " (0.." + (parent.length - 1) + ")");
        }
    }
}