package newsaggregator;

import newsaggregator.dataaccess.ImportData;
import newsaggregator.jsonfilewriter.JSONFileWriter;
import newsaggregator.post.Post;
import newsaggregator.webcrawling.Crawler;
import newsaggregator.webcrawling.rssloader.RSSReader;

public class Main {
    public static void main(String[] args) {
        Crawler rss = new RSSReader();
        rss.crawl();
        JSONFileWriter.writeFileFromPost(rss.getPostList(), "src/main/resources/data.json");
        try {
            ImportData.importJSON("src/main/resources/data.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
