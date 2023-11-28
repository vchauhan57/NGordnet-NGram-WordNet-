package ngordnet.proj2b_testing;

import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.main.Graph;
import ngordnet.main.HyponymsHandler;
import ngordnet.ngrams.NGramMap;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        Graph graph = new Graph(synsetFile, hyponymFile, wordFile, countFile);
        NGramMap ngm = new NGramMap(wordFile, countFile);
        return new HyponymsHandler(graph, ngm);
    }
}
