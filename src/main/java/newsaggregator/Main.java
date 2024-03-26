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
        rss.getPostList().forEach(Post::display);
        JSONFileWriter.writeFileFromPost(rss.getPostList(), "src/main/resources/data/upstream/data.json");
        try {
            ImportData.importJSON("src/main/resources/data/upstream/data.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
