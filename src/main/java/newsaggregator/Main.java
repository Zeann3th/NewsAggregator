package newsaggregator;

import newsaggregator.dataaccess.ImportData;
import newsaggregator.dataaccess.QueryData;
import newsaggregator.jsonfilewriter.JSONFileWriter;
import newsaggregator.posts.Post;
import newsaggregator.webcrawling.Crawler;
import newsaggregator.webcrawling.dynamic.CoinTelegraphCrawler;
import newsaggregator.webcrawling.rssloader.RSSReader;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Crawler rss = new RSSReader();
        rss.crawl();
        Crawler ctc = new CoinTelegraphCrawler();
        ctc.crawl();
        List<Post> newlist = new ArrayList<>();
        newlist.addAll(rss.getPostList());
        newlist.addAll(ctc.getPostList());
        JSONFileWriter writer = new JSONFileWriter();
        writer.writeFile(newlist);
        try {
            ImportData importData = new ImportData();
            importData.importJSON("src/main/resources/data.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
