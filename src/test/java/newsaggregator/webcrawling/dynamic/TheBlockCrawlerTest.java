package newsaggregator.webcrawling.dynamic;

import newsaggregator.post.Post;
import newsaggregator.webcrawling.Crawler;
import org.junit.jupiter.api.Test;

class TheBlockCrawlerTest {

    @Test
    void crawl() {
        Crawler theBlockCrawler = new TheBlockCrawler();
        theBlockCrawler.crawl();
        theBlockCrawler.getPostList().forEach(Post::display);
    }
}