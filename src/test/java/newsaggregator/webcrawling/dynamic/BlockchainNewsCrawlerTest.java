package newsaggregator.webcrawling.dynamic;

import newsaggregator.post.Post;
import newsaggregator.webcrawling.Crawler;
import org.junit.jupiter.api.Test;

class BlockchainNewsCrawlerTest {

    @Test
    void crawl() {
        Crawler blockchainNewsCrawler = new BlockchainNewsCrawler();
        blockchainNewsCrawler.crawl();
        blockchainNewsCrawler.getPostList().forEach(Post::display);
    }
}