package newsaggregator;

import newsaggregator.dataaccess.ImportData;
import newsaggregator.jsonfilewriter.JSONFileWriter;
import newsaggregator.post.Post;
import newsaggregator.webcrawling.Crawler;
import newsaggregator.webcrawling.dynamic.BlockchainNewsCrawler;
import newsaggregator.webcrawling.dynamic.CoinTelegraphCrawler;
import newsaggregator.webcrawling.dynamic.TheBlockCrawler;
import newsaggregator.webcrawling.rssloader.RSSReader;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Crawler rss = new RSSReader();
        rss.crawl();
        Crawler ctc = new CoinTelegraphCrawler();
        ctc.crawl();
        Crawler theBlock = new TheBlockCrawler();
        theBlock.crawl();
        Crawler bnc = new BlockchainNewsCrawler();
        bnc.crawl();
        List<Post> newlist = new ArrayList<>();
        newlist.addAll(rss.getPostList());
        newlist.addAll(ctc.getPostList());
        newlist.addAll(theBlock.getPostList());
        newlist.addAll(bnc.getPostList());
        newlist.forEach(Post::display);
        JSONFileWriter.writeFileFromPost(newlist, "src/main/resources/data.json");
        try {
            ImportData.importJSON("src/main/resources/data.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
