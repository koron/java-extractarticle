package net.kaoriya.extractarticle;

import java.io.File;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArticleExtractorTest {

    static class Dataset {
        String name;
        boolean valid;
        boolean nan;
        Dataset(String name, boolean valid, boolean nan) {
            this.name = name;
            this.valid = valid;
            this.nan = nan;
        }
        Dataset(String name, boolean valid) {
            this(name, valid, false);
        }
    }

    @Test
    public void extractDataset() throws Exception {
        for (var d : new Dataset[]{
            new Dataset("000001.html", true),
            new Dataset("000002.html", true),
            new Dataset("000003.html", false),
            new Dataset("000004.html", true),
            new Dataset("000005.html", true),
            new Dataset("000006.html", true),
            new Dataset("000007.html", true),
            new Dataset("000008.html", true),
            new Dataset("000009.html", true),
            new Dataset("000010.html", true),
            new Dataset("000011.html", true),
            new Dataset("000012.html", true),
            new Dataset("000013.html", false, true),
            new Dataset("000014.html", false, true),
            new Dataset("000015.html", true),
            new Dataset("000016.html", true),
            new Dataset("000017.html", true),
            new Dataset("000018.html", true),
            new Dataset("000019.html", true),
            new Dataset("000020.html", true),
            new Dataset("000021.html", true),
            new Dataset("000022.html", true),
            new Dataset("000023.html", true),
            new Dataset("000024.html", true),
            new Dataset("000025.html", true),
            new Dataset("000026.html", true),
            new Dataset("000027.html", false, true),
            new Dataset("000028.html", false, true),
            new Dataset("000029.html", false, true),
            new Dataset("000030.html", false, true),
            new Dataset("000031.html", false, true),
            new Dataset("000032.html", false, true),
            new Dataset("000033.html", true),
            new Dataset("000034.html", true),
            new Dataset("000035.html", true),
            new Dataset("000036.html", true),
            new Dataset("000037.html", true),
            new Dataset("000038.html", true),
            new Dataset("000039.html", true),
            new Dataset("000040.html", false, true),
            new Dataset("000041.html", false, true),
        }) {
            var s = getClass().getClassLoader().getResource(d.name);
            var f = new File(s.toURI());
            var r = ArticleExtractor.extract(f);
            System.out.println(String.format("OK\t%s\t%g\t%d\t%d", f.getName(), r.score, r.text.length(), r.desc.length()));
            if (d.valid) {
                assertTrue(r.score >= 0.8, String.format("score of %s should be >= 0.8: %g", d.name, r.score));
            } else if (d.nan) {
                assertTrue(Double.isNaN(r.score), String.format("score of %s should be NaN: %g", d.name, r.score));
            } else {
                assertTrue(r.score < 0.8, String.format("score of %s should be < 0.8: %g", d.name, r.score));
            }
        }
    }
}
