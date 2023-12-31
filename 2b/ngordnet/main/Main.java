package ngordnet.main;

import ngordnet.browser.NgordnetServer;
import ngordnet.ngrams.NGramMap;

public class Main {
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();
        String wordFile = "./data/ngrams/top_49887_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";

        String synsetFile = "./data/wordnet/synsets.txt";
        String hyponymFile = "./data/wordnet/hyponyms.txt";

        NGramMap ngm = new NGramMap(wordFile, countFile);
        Graph graph  = new Graph(synsetFile, hyponymFile, wordFile, countFile);

        hns.startUp();
        hns.register("hyponyms", new HyponymsHandler(graph, ngm));
        hns.register("historytext", new HistoryTextHandler(ngm));
    }
}
