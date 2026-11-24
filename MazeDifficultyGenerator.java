package maze;

import graph.Graph;

/**
 * BONUS — génération d'un labyrinthe selon un niveau de difficulté.
 *
 * <p>===== CLASSE À COMPLÉTER PAR LES ÉTUDIANTS (bonus seulement) =====</p>
 *
 * <p>Cette classe n'est jamais appelée par le banc d'essai obligatoire ({@code Main}).
 * Pour la tester séparément, utilisez {@code BonusMain}.</p>
 */
public final class MazeDifficultyGenerator {

    /** Nombre maximal de labyrinthes candidats à essayer (garantit que la méthode termine). */
    public static final int MAX_ATTEMPTS = 50;

    private MazeDifficultyGenerator() {
    }

    /**
     * Génère un labyrinthe (parfait) correspondant au niveau de difficulté demandé.
     *
     * <p>Exigences :</p>
     * <ul>
     *   <li>choisir et <b>justifier dans votre rapport</b> vos propres métriques de difficulté
     *       (longueur du chemin entrée-sortie, nombre d'impasses, changements de direction, etc.) ;</li>
     *   <li>réutiliser {@code Prim}, {@code Kruskal} et {@code MazeDFS} au lieu de les réécrire ;</li>
     *   <li>essayer au plus {@link #MAX_ATTEMPTS} candidats (par exemple en variant la graine
     *       ou en regénérant les poids avec {@code GridGraphFactory.generate(graph.getRows(), graph.getCols(), nouvelleGraine)}) ;</li>
     *   <li>si le niveau demandé n'est pas atteint après {@link #MAX_ATTEMPTS} tentatives, adopter un
     *       comportement documenté ci-dessous (retourner le meilleur candidat, ou lever une exception).</li>
     * </ul>
     *
     * <p>Convention de la classe {@code Maze} : l'entrée est la cellule 0 (en haut à gauche) et la sortie
     * est la dernière cellule (en bas à droite), voir {@code Maze.getEntrance()} et {@code Maze.getExit()}.</p>
     *
     * <p>Les dimensions de la grille sont accessibles par {@code graph.getRows()} et
     * {@code graph.getCols()} ; on convertit une liste d'arêtes en labyrinthe avec
     * {@code Maze.fromEdges(rows, cols, edges)}.</p>
     *
     * <p>TODO : documenter ici le comportement choisi lorsque le niveau n'est pas atteignable.</p>
     *
     * @param graph      graphe de grille connexe (construit par {@code GridGraphFactory})
     * @param difficulty niveau demandé
     * @param seed       graine (le résultat doit être reproductible)
     * @return un labyrinthe parfait
     */
    public static Maze generate(Graph graph, Difficulty difficulty, long seed) {
        // TODO (bonus) : implanter la génération selon la difficulté.
        throw new UnsupportedOperationException(
                "TODO : MazeDifficultyGenerator.generate() (bonus) n'est pas encore implantée.");
    }
}
