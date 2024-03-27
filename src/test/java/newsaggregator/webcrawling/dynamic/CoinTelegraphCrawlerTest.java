package newsaggregator.webcrawling.dynamic;

import newsaggregator.post.Post;
import newsaggregator.webcrawling.Crawler;
import org.junit.jupiter.api.Test;

class CoinTelegraphCrawlerTest {

    @Test
    void crawl() {
        Crawler ctc = new CoinTelegraphCrawler();
        ctc.crawl();
        ctc.getPostList().forEach(Post::display);
    }
}