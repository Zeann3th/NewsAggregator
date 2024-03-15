package newsaggregator;

import newsaggregator.jsonfilewriter.JSONFileWriter;
import newsaggregator.webcrawling.Crawler;
import newsaggregator.webcrawling.rssloader.RSSReader;

public class Main {
    public static void main(String[] args) {
        Crawler rss = new RSSReader();
        rss.crawl();
        JSONFileWriter writer = new JSONFileWriter();
        writer.writeFile(rss.getPostList());
    }
}
