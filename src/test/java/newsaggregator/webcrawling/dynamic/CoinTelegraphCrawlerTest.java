package newsaggregator.webcrawling.dynamic;

import newsaggregator.posts.Post;
import newsaggregator.webcrawling.Crawler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoinTelegraphCrawlerTest {

    @Test
    void crawl() {
        Crawler ctc = new CoinTelegraphCrawler();
        ctc.crawl();
        ctc.getPostList().forEach(Post::display);
    }
}