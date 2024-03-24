package newsaggregator.webcrawling.rssloader;

import newsaggregator.post.Post;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RSSReaderTest {

    @Test
    void crawl() {
        var rss = new RSSReader();
        rss.crawl();
        rss.getPostList().forEach(Post::display);
    }
}