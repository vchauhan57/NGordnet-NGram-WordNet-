package ngordnet.main;

import edu.princeton.cs.algs4.In;
import ngordnet.ngrams.NGramMap;

import java.util.*;

public class Graph {
    public HashMap<String, ArrayList<Integer>> allOccurrences;
    public HashMap<Integer, Node> synsets;

    public Graph(String synsetFile, String hyponymFile, String wordFile, String countFile) {
        allOccurrences = new HashMap<>();
        synsets = new HashMap<>();
        In reader = new In(synsetFile);
        In reader2 = new In(hyponymFile);
        while (reader.hasNextLine()) {
            String lineString = reader.readLine();
            addNode(lineString);
        }
        while (reader2.hasNextLine()) {
            String lineString = reader2.readLine();
            String[] lineArray = lineString.split(",");
            int index = Integer.parseInt(lineArray[0]);
            for (int i = 1; i < lineArray.length; i++) {
                synsets.get(index).children.add(Integer.parseInt(lineArray[i]));
            }
        }
    }
    private void addNode(String line) {
        String[] lineArray = line.split(",");
        String[] keyArray = lineArray[1].split(" ");
        int index = Integer.parseInt(lineArray[0]);
        ArrayList<Integer> holder = new ArrayList<>();
        Node x = new Node(index, keyArray, holder);
        synsets.put(Integer.parseInt(lineArray[0]), x);
        for (String i : keyArray) {
            if (allOccurrences.containsKey(i)) {
                allOccurrences.get(i).add(index);
            } else {
                ArrayList<Integer> lst = new ArrayList<>();
                lst.add(index);
                allOccurrences.put(i, lst);
            }
        }
    }
    public class Node {
        public String[] keys;
        public int number;
        public ArrayList<Integer> children;
        public Node(int number, String[] key, ArrayList<Integer> children) {
            this.number = number;
            this.keys = key;
            this.children = children;
        }
    }
}
