package newsaggregator.webcrawling.rssloader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RSSReaderTest {

    @Test
    void crawl() {
        var rss = new RSSReader();
        rss.crawl();
    }
}