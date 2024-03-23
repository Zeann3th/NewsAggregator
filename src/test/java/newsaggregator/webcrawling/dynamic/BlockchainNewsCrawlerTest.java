package newsaggregator.webcrawling.dynamic;

import newsaggregator.posts.Post;
import newsaggregator.webcrawling.Crawler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockchainNewsCrawlerTest {

    @Test
    void crawl() {
        Crawler blockchainNewsCrawler = new BlockchainNewsCrawler();
        blockchainNewsCrawler.crawl();
        blockchainNewsCrawler.getPostList().forEach(Post::display);
    }
}