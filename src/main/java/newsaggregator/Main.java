package newsaggregator;


import newsaggregator.post.Post;
import newsaggregator.webcrawling.Crawler;
import newsaggregator.webcrawling.rssloader.RSSReader;

public class Main {
    public static void main(String[] args) {
        Crawler rss = new RSSReader();
        rss.crawl();
        rss.getPostList().forEach(Post::display);
    }
}
