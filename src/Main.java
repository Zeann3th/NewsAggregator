import posts.Post;
import util.JSONFileWriter;
import webcrawling.CoinTelegraphCrawler;
import webcrawling.Crawler;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Crawler crawler1 = new CoinTelegraphCrawler();
        crawler1.crawl();
        List<Post> postList = crawler1.getPostList();
        JSONFileWriter jfw = new JSONFileWriter();
        jfw.writeFile(postList);
    }
}