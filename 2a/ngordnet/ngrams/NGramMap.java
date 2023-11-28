package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    private HashMap<String, TimeSeries> main;
    private TimeSeries countsMain;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        main = new HashMap<>();
        In in = new In(wordsFilename);
        In in2 = new In(countsFilename);
        countsMain = new TimeSeries();
        TimeSeries wordData = new TimeSeries();
        if (in.hasNextLine()) {
            String line = in.readLine();
            String[] lineArray = line.split("\\s+");
            String word = lineArray[0];
            Integer year = Integer.parseInt(lineArray[1]);
            Double data = Double.parseDouble(lineArray[2]);
            wordData.put(year, data);
            main.put(word, wordData);
        }
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] lineArray2 = line.split("\\s+");
            String word = lineArray2[0];
            Integer year = Integer.parseInt(lineArray2[1]);
            Double data = Double.parseDouble(lineArray2[2]);
            if (main.containsKey(word)) {
                main.get(word).put(year, data);
            } else {
                wordData = new TimeSeries();
                wordData.put(year, data);
                main.put(word, wordData);
            }
        }

        while (in2.hasNextLine()) {
            String line = in2.readLine();
            String[] lineArray2 = line.split(",");
            Integer year = Integer.parseInt(lineArray2[0]);
            Double data = Double.parseDouble(lineArray2[1]);
            countsMain.put(year, data);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries blank = new TimeSeries();
        if (!main.containsKey(word)) {
            return blank;
        }
        TimeSeries rv = new TimeSeries(main.get(word), startYear, endYear);
        return rv;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        TimeSeries rv = new TimeSeries();
        if (!main.containsKey(word)) {
            return rv;
        }
        List<Integer> years = main.get(word).years();
        List<Double> data = main.get(word).data();
        for (int i = 0; i < years.size(); i++) {
            rv.put(years.get(i), data.get(i));
        }
        return rv;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries rv = new TimeSeries();
        if (main.size() == 0) {
            return rv;
        }
        List<Integer> years = countsMain.years();
        List<Double> data = countsMain.data();
        for (int i = 0; i < years.size(); i++) {
            rv.put(years.get(i), data.get(i));
        }
        return rv;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries rv = new TimeSeries();
        if (!main.containsKey(word)) {
            return rv;
        }
        TimeSeries wordFreq = countHistory(word, startYear, endYear);
        List<Integer> years = wordFreq.years();
        for (int i = 0; i < years.size(); i++) {
            rv.put(years.get(i), wordFreq.get(years.get(i)) / countsMain.get(years.get(i)));
        }
        return rv;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries rv = new TimeSeries();
        if (!main.containsKey(word)) {
            return rv;
        }
        TimeSeries wordFreq = countHistory(word);
        List<Integer> years = wordFreq.years();
        for (int i = 0; i < years.size(); i++) {
            rv.put(years.get(i), wordFreq.get(years.get(i)) / countsMain.get(years.get(i)));
        }
        return rv;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries rv = new TimeSeries();
        for (int i = startYear; i <= endYear; i++) {
            double sum = 0;
            if (countsMain.containsKey(i)) {
                for (String x : words) {
                    if (main.containsKey(x) && main.get(x).containsKey(i)) {
                        sum += main.get(x).get(i) / countsMain.get(i);
                    }
                }
            }
            if (sum > 0.0) {
                rv.put(i, sum);
            }
        }
        return rv;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries rv = new TimeSeries();
        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            double sum = 0;
            if (countsMain.containsKey(i)) {
                for (String x : words) {
                    if (main.containsKey(x) && main.get(x).containsKey(i)) {
                        sum += main.get(x).get(i) / countsMain.get(i);
                    }
                }
            }
            if (sum > 0.0) {
                rv.put(i, sum);
            }
        }
        return rv;
    }
}
