package newsaggregator.jsonfilewriter;

import newsaggregator.dataaccess.ReadData;
import newsaggregator.webcrawling.rssloader.RSSReader;
import org.junit.jupiter.api.Test;

class JSONFileWriterTest {

    @Test
    void writeFileFromPost() {
        var rss = new RSSReader();
        rss.crawl();
        JSONFileWriter.writeFileFromPost(rss.getPostList(),"src/test/resources/data/test_post_data.json");
    }

    @Test
    void writeFileFromDocument() {
        var data = new ReadData();
        data.search("blockchain", "desc");
        JSONFileWriter.writeFileFromDocument(data.getQueryData(), "src/test/resources/data/test_document_data.json");
    }
}