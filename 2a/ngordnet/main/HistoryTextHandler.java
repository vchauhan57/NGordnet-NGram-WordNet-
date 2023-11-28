package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap main;
    public HistoryTextHandler(NGramMap map) {
        this.main = map;
    }
    @Override
    public String handle(NgordnetQuery q) {
        String rv = "";
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        for (int i = 0; i < words.size(); i++) {
            rv += words.get(i);
            rv += ": ";
            rv += main.weightHistory(words.get(i), startYear, endYear).toString();
            rv += "\n";
        }
        return rv;
    }
}
