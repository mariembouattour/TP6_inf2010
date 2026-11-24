import graph.Graph;
import graph.GridGraphFactory;
import maze.Difficulty;
import maze.Maze;
import maze.MazeDifficultyGenerator;

/**
 * Test séparé du bonus (génération selon la difficulté). N'a aucun effet sur le banc d'essai.
 *
 * <p>Fichier fourni : ne pas modifier.</p>
 */
public class BonusMain {

    public static void main(String[] args) {
        int rows = 12;
        int cols = 20;
        Graph graph = GridGraphFactory.generate(rows, cols, 42L);

        for (Difficulty difficulty : Difficulty.values()) {
            System.out.println("=== Difficulté : " + difficulty + " ===");
            try {
                Maze maze = MazeDifficultyGenerator.generate(graph, difficulty, 1L);
                maze.display();
                System.out.println("Labyrinthe parfait : " + maze.isPerfectMaze());
            } catch (UnsupportedOperationException e) {
                System.out.println(e.getMessage());
                return;
            }
            System.out.println();
        }
    }
}
