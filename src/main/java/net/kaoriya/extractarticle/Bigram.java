package net.kaoriya.extractarticle;

import java.util.Map;
import java.util.HashMap;

/**
 * Bigram provides bi-gram index for a string.
 */
public class Bigram {
    /** index is a map for bi-gram index. */
    public final Map<String, Integer> index = new HashMap<String, Integer>();

    /** Bigram creates an index of bi-gram. */
    public Bigram(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            var sub = str.substring(i, i+2);
            int cnt = 0;
            if (index.containsKey(sub)) {
                cnt = index.get(sub);
            }
            index.put(sub, cnt + 1);
        }
    }

    /** count counts keys in index. */
    public int count() {
        return index.size();
    }
}
