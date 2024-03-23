package newsaggregator.webcrawling.dynamic;

import newsaggregator.posts.Post;
import newsaggregator.webcrawling.Crawler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TheBlockCrawlerTest {

    @Test
    void crawl() {
        Crawler theBlockCrawler = new TheBlockCrawler();
        theBlockCrawler.crawl();
        theBlockCrawler.getPostList().forEach(Post::display);
    }
}