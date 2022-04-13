package net.kaoriya.extractarticle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Selector;

/** ArticleExtractor extracts artcile in raw text. */
public class ArticleExtractor {

    /** Result is a result of artcile extraction. */
    public static class Result  {
        /** Extracted text of article . */
        public String text;
        /** Description in meta. */
        public String desc;
        /** Score for extraction result. */
        public double score;
        /** Result creates a result object. */
        public Result(String text, String desc, double score) {
            this.text = text;
            this.desc = desc;
            this.score = score;
        }
    }

    static String regulateText(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder b = new StringBuilder();
        boolean prev = false;
        for (char ch : s.toCharArray()) {
            if (ch == '　') {
                ch = ' ';
            }
            switch (ch) {
                case '\t':
                case '\n':
                case '\r':
                    prev = false;
                    continue;
                case ' ':
                    if (prev) {
                        continue;
                    }
                    prev = true;
                    break;
                default:
                    prev = false;
                    break;
            }
            b.append(ch);
        }
        return b.toString();
    }

    static String extractMetaDesc(Document doc) {
        var el = Selector.selectFirst("meta[name='description']", doc);
        if (el == null) {
            return "";
        }
        return el.attributes().get("content");
    }

    static double calcScore(String desc, String text) {
        var descIdx = new Bigram(desc);
        var textIdx = new Bigram(text);
        return calcFrac(descIdx, textIdx);
    }

    static double calcFrac(Bigram base, Bigram target) {
        int cnt = 0;
        for (var e : base.index.entrySet()) {
            if (target.index.containsKey(e.getKey())) {
                cnt++;
            }
        }
        return (double)cnt / (double)base.count();
    }

    /** extracts an article from Document. */
    public static Result extract(Document doc) {
        var r4j = new Readability4J("", doc);
        var article = r4j.parse();
        var text = regulateText(article.getTextContent());
        var desc = regulateText(extractMetaDesc(doc));
        var score = calcScore(desc, text);
        return new Result(text, desc, score);
    }

    /** extracts an article from HTML string. */
    public static Result extract(String html) {
        return extract(Jsoup.parse(html));
    }

    /** extracts an article from a File. */
    public static Result extract(File file) throws IOException {
        return extract(Jsoup.parse(file, "UTF-8"));
    }

    /** extracts an article from a InputStream. */
    public static Result extract(InputStream in) throws IOException {
        return extract(Jsoup.parse(in, "UTF-8", ""));
    }
}
