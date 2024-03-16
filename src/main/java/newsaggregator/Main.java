package newsaggregator;

import newsaggregator.dataaccess.ImportData;
import newsaggregator.dataaccess.QueryData;
import newsaggregator.jsonfilewriter.JSONFileWriter;
import newsaggregator.posts.Post;
import newsaggregator.webcrawling.Crawler;
import newsaggregator.webcrawling.dynamic.CoinTelegraphCrawler;
import newsaggregator.webcrawling.rssloader.RSSReader;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        Crawler rss = new RSSReader();
//        rss.crawl();
//        Crawler ctc = new CoinTelegraphCrawler();
//        ctc.crawl();
//        List<Post> newlist = new ArrayList<>();
//        newlist.addAll(rss.getPostList());
//        newlist.addAll(ctc.getPostList());
//        JSONFileWriter writer = new JSONFileWriter();
//        writer.writeFile(newlist, "src/main/resources/data.json"
//        try {
//            ImportData importData = new ImportData();
//            importData.importJSON("src/main/resources/data.json");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            QueryData queryData = new QueryData();
            queryData.read("dao", "desc");
            List<Document> queryArticles = queryData.getQueryData();
            JSONFileWriter writer = new JSONFileWriter();
            writer.writeFileFromDocument(queryArticles, "src/main/resources/queryData.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
