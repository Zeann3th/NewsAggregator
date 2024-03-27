package newsaggregator;

import newsaggregator.dataaccess.ReadData;

public class Main {
    public static void main(String[] args) {
//        Crawler rss = new RSSReader();
//        rss.crawl();
//        JSONFileWriter.writeFileFromPost(rss.getPostList(), "src/main/resources/data/upstream/data.json");
//        try {
//            ImportData.importJSON("src/main/resources/data/upstream/data.json");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ImportData.createSearchIndex();
        ReadData qd = new ReadData();
        qd.autoComplete("AI");
    }
}
