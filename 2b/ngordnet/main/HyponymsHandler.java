package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private Graph graph;
    private NGramMap n;
    public HyponymsHandler(Graph graph, NGramMap n) {
        this.graph = graph;
        this.n = n;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> rv;
        HashSet<String> hs;
        if (q.words().size() > 1) {
            List<String> holder = new ArrayList<>();
            for (String i : q.words()) {
                holder.add(i);
            }
            hs = handleList(holder, q);
        } else {
            hs = handleHelper(q.words().get(0), q);
        }
        rv = new ArrayList<>(hs);
        if (q.k() != 0) {
            rv = finalSort(rv, q);
        }
        Collections.sort(rv);
        return rv.toString();
    }
    private HashSet<String> handleHelper(String word, NgordnetQuery q) {
        HashSet<String> list = new HashSet<>();
        if (graph.allOccurrences.get(word) == null) {
            return list;
        }
        for (Integer i : graph.allOccurrences.get(word)) {
            addAllChildren(i, list);
        }
        return list;
    }
    private void addAllChildren(Integer index, HashSet<String> lst) {
        Graph.Node node = graph.synsets.get(index);
        for (String i : node.keys) {
            lst.add(i);
        }
        if (node.children == null) {
            return;
        }
        for (Integer i : node.children) {
            addAllChildren(i, lst);
        }
    }
    private HashSet<String> handleList(List<String> words, NgordnetQuery q) {
        List<HashSet> allLists = new ArrayList<>();
        for (String i : words) {
            allLists.add(handleHelper(i, q));
        }
        HashSet<String> compList = allLists.get(0);
        HashSet<String> rv = new HashSet<>();
        for (String i : compList) {
            int count = 0;
            for (HashSet<String> x : allLists) {
                if (!x.contains(i)) {
                    count = 1;
                }
            }
            if (count == 0) {
                rv.add(i);
            }
        }
        return rv;
    }
    private List<String> finalSort(List<String> list, NgordnetQuery q) {
        int k = q.k();
        int startYear = q.startYear();
        int endYear = q.endYear();
        TreeMap<Double, String> tm = makeCountMap(list, startYear, endYear);
        List<String> rv2 = new ArrayList<>();
        double count = 0;
        for (Double key : tm.keySet()) {
            if (count < k) {
                rv2.add(tm.get(key));
            }
            count++;
        }
        return rv2;

    }

    private String findMax(TreeMap<String, Integer> tm) {
        int holder = 0;
        String stringHolder = "test word";
        for (String i : tm.keySet()) {
            String word = i;
            int val = tm.get(i);
            if (val > holder) {
                holder = val;
                stringHolder = word;
            }
        }
        if (holder == 0) {
            return null;
        }
        return stringHolder;
    }
    private TreeMap<Double, String> makeCountMap(List<String> list, int startYear, int endYear) {
        TreeMap<Double, String> tm = new TreeMap<>(Collections.reverseOrder());
        for (String i : list) {
            double count = 0;
            TimeSeries x = n.countHistory(i, startYear, endYear);
            for (Integer year : x.keySet()) {
                count += x.get(year);
            }
            if (count > 0) {
                tm.put(count, i);
            }
        }
        return tm;
    }

}
