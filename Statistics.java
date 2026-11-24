package experiment;

import java.util.Arrays;

/**
 * Fonctions statistiques utilisées par le banc d'essai.
 *
 * <p>Classe fournie : ne pas modifier.</p>
 */
public final class Statistics {

    private Statistics() {
    }

    /** Médiane (le tableau reçu n'est pas modifié). */
    public static double median(double[] values) {
        checkNotEmpty(values);
        double[] sorted = Arrays.copyOf(values, values.length);
        Arrays.sort(sorted);
        int n = sorted.length;
        if (n % 2 == 1) {
            return sorted[n / 2];
        }
        return (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
    }

    /** Moyenne arithmétique. */
    public static double average(double[] values) {
        checkNotEmpty(values);
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    /** Écart-type de l'échantillon (division par n - 1) ; 0 s'il n'y a qu'une valeur. */
    public static double standardDeviation(double[] values) {
        checkNotEmpty(values);
        if (values.length == 1) {
            return 0;
        }
        double mean = average(values);
        double sumSquares = 0;
        for (double v : values) {
            sumSquares += (v - mean) * (v - mean);
        }
        return Math.sqrt(sumSquares / (values.length - 1));
    }

    private static void checkNotEmpty(double[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Tableau de valeurs vide");
        }
    }
}
