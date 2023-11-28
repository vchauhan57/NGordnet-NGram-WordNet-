package ngordnet.ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        List<Integer> years = ts.years();
        List<Double> data = ts.data();
        for (int i = 0; i < years.size(); i++) {
            if (years.get(i) >= startYear && years.get(i) <= endYear) {
                this.put(years.get(i), data.get(i));
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        List<Integer> years = new ArrayList<>();
        for (int i = MIN_YEAR; i < MAX_YEAR + 1; i++) {
            if (this.containsKey(i)) {
                years.add(i);
            }
        }
        return years;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List<Double> data = new ArrayList<>();
        for (int i = 0; i < this.years().size(); i++) {
            data.add(this.get(years().get(i)));
        }
        return data;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries rv = new TimeSeries();
        List<Integer> years = this.years();
        List<Double> data = this.data();
        for (int i = 0; i < years.size(); i++) {
            rv.put(years.get(i), data.get(i));
        }
        List<Integer> tsYears = ts.years();
        for (int i = 0; i < tsYears.size(); i++) {
            if (rv.containsKey(tsYears.get(i))) {
                rv.put(tsYears.get(i), rv.get(tsYears.get(i)) + ts.get(tsYears.get(i)));
            } else {
                rv.put(tsYears.get(i), ts.get(tsYears.get(i)));
            }
        }
        return rv;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        List<Integer> thisYears = this.years();
        for (int i = 0; i < thisYears.size(); i++) {
            if (!ts.containsKey(thisYears.get(i))) {
                throw new IllegalArgumentException();
            }
        }
        TimeSeries rv = new TimeSeries();
        for (int i = 0; i < thisYears.size(); i++) {
            rv.put(thisYears.get(i), this.get(thisYears.get(i)) / ts.get(thisYears.get(i)));
        }
        return rv;
    }
}
