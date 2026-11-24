# TP6 — Arbres couvrants minimaux et labyrinthes : projet de départ

Ce document décrit le code du projet. Les consignes du travail se trouvent dans l'énoncé.

## Structure

```text
TP6/
├── src/
│   ├── graph/         Graph, Edge, GridGraphFactory
│   ├── algorithms/    Prim, Kruskal, FastUnionFind, MazeDFS
│   ├── maze/          Maze, Difficulty, MazeDifficultyGenerator
│   ├── experiment/    Benchmark, BenchmarkResult, Statistics, CSVWriter, TreeValidator
│   ├── Main.java      lance les expériences et génère le CSV
│   └── BonusMain.java teste séparément MazeDifficultyGenerator
├── output/            output.csv est créé ici à l'exécution
└── README.md
```

Les fonctions à compléter sont signalées par des commentaires `TODO` dans le code.

## Importer le projet dans IntelliJ IDEA

1. Décompressez l'archive du projet.
2. *File > Open...* et choisissez le dossier `TP6`.
3. Clic droit sur le dossier `src` > *Mark Directory as* > *Sources Root*.
4. *File > Project Structure > Project* : choisissez un SDK Java (version 11 ou plus récente ; le code est
   compatible avec Java 8).
5. Ouvrez `src/Main.java`, cliquez sur la flèche verte à côté de `main`, puis *Run 'Main.main()'*.

Le répertoire de travail de la configuration d'exécution doit être le dossier `TP6` (c'est le cas par
défaut dans IntelliJ), afin que le fichier soit créé dans `TP6/output/`. Si ce n'est pas le cas :
*Run > Edit Configurations... > Working directory*.

Aucune bibliothèque externe n'est nécessaire.

En ligne de commande, depuis le dossier `TP6` :

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out Main
```

(Sous Windows PowerShell : `javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName`.)

## Ce que fait `Main`

`Main` appelle `Benchmark.runExperiments()`, qui exécute les étapes suivantes pour chaque dimension
prédéfinie (de 10x10 à 600x600) :

1. génération de la grille et des poids pseudo-aléatoires (graines fixes : les mêmes graphes à chaque
   exécution) ;
2. chauffe de la JVM, puis mesure de Prim, Kruskal et du DFS avec `System.nanoTime()` (11 exécutions
   chacun, ordre des algorithmes en rotation) ;
3. validation de chaque résultat, hors chronométrage : `V - 1` arêtes, arêtes existantes, aucun cycle,
   toutes les cellules accessibles, labyrinthe parfait (`Maze.isPerfectMaze()`). Prim et Kruskal
   doivent aussi produire des arbres de même poids total ;
4. calcul des médianes et écriture de `output/output.csv`, remplacé à chaque exécution.

Si un résultat est invalide ou si une fonction n'est pas encore implantée, l'expérience s'arrête avec un
message d'erreur et **aucun CSV n'est écrit**. La génération des graphes, la validation et la conversion
en `Maze` ne sont jamais incluses dans les temps mesurés.

Les dimensions, le nombre de répétitions, la chauffe et les graines sont regroupés en tête de
`Benchmark.java`. L'exécution complète dure environ une minute.

## Format de `output/output.csv`

```csv
rows,cols,V,E,prim_median_ms,kruskal_median_ms,dfs_median_ms
```

| Colonne | Signification |
|---|---|
| `rows`, `cols` | dimensions de la grille |
| `V` | nombre de sommets (`rows * cols`) |
| `E` | nombre d'arêtes de la grille |
| `prim_median_ms` | temps médian de Prim, en millisecondes |
| `kruskal_median_ms` | temps médian de Kruskal, en millisecondes |
| `dfs_median_ms` | temps médian du DFS aléatoire, en millisecondes |

Le séparateur décimal est toujours le point. Le fichier ne contient aucune estimation de mémoire.

## Conventions du code

* Sommet de la cellule (`row`, `col`) : `row * cols + col`.
* Les poids sont des entiers entre 1 et 1000.
* **Entrée** du labyrinthe : cellule en haut à gauche (identifiant 0). **Sortie** : cellule en bas à
  droite (identifiant `rows * cols - 1`). Voir `Maze.getEntrance()` et `Maze.getExit()`.
* Chaque arête non orientée apparaît une fois dans `graph.getEdges()` et deux fois dans la liste
  d'adjacence (une fois par extrémité). Pour toute arête `e` de `graph.getNeighbors(v)`,
  `e.getSource() == v` et `e.getDestination()` est le voisin.
* Les listes retournées par `getNeighbors` et `getEdges` sont non modifiables.
* `Edge.compareTo` trie par poids croissant (puis par extrémités, pour un tri déterministe).
* `MazeDFS` ignore les poids : son arbre est un arbre couvrant valide, mais pas minimal.
* Un graphe de grille connaît ses dimensions : `graph.getRows()`, `graph.getCols()`.

## Visualiser un labyrinthe

```java
Graph g = GridGraphFactory.generate(10, 15, 42L);
Maze maze = Maze.fromEdges(g.getRows(), g.getCols(), MazeDFS.generate(g, 1L));
maze.display();                              // E = entrée, S = sortie
System.out.println(maze.isPerfectMaze());
```

À réserver aux petites grilles.

## `BonusMain`

`BonusMain` génère un labyrinthe pour chacun des trois niveaux de `Difficulty` avec
`MazeDifficultyGenerator.generate`, l'affiche et vérifie qu'il est parfait. Il est indépendant de `Main`
et n'a aucun effet sur les mesures du banc d'essai.
