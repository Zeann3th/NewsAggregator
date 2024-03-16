package newsaggregator.webcrawling.rssloader;

import javax.xml.parsers.*;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import newsaggregator.posts.Author;
import newsaggregator.posts.Post;
import newsaggregator.webcrawling.Crawler;
import org.w3c.dom.*;

public class RSSReader extends Crawler {
    /**
     * Lớp RSSReader thực hiện việc đọc file XML từ các nguồn RSS được lưu trữ trong file webSources.txt
     * và trả về một danh sách các bài báo được lưu trữ trong các file XML này
     * @return danh sách các bài báo được lưu trữ trong các file XML
     * @throws Exception
     * @author: Lý Hiển Long
     */
    @Override
    public void crawl() {
//        RSSSync rssSync = new RSSSync();
        List<Post> postList = new ArrayList<>();
        try {
            File newsList = new File("src/main/resources/RSSData/webSources.txt");
            Scanner newsListScanner = new Scanner(newsList);
            while (newsListScanner.hasNextLine()) {
                String urlString = newsListScanner.nextLine();
                String domainString = URI.create(urlString).getHost();
                RSSSync.getNewUpdate(urlString, "src/main/resources/RSSData/tmp-cache/%s.xml".formatted(domainString));
                RSSReader rssReader = new RSSReader();
                List<Post> currentPostList = rssReader.parseXML("src/main/resources/RSSData/tmp-cache/%s.xml".formatted(domainString));
                postList.addAll(currentPostList);
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        setPostList(postList);
    }

    public List<Post> parseXML(String URIString) {
        List<Post> currentPostList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(URIString);
            NodeList items = doc.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Node item = items.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) item;
                    String title = elem.getElementsByTagName("title").item(0).getTextContent();
                    String link = elem.getElementsByTagName("link").item(0).getTextContent();
                    String description = elem.getElementsByTagName("description").item(0).getTextContent();
                    String[] categories = new String[elem.getElementsByTagName("category").getLength()];
                    for (int j = 0; j < elem.getElementsByTagName("category").getLength(); j++) {
                        String category = elem.getElementsByTagName("category").item(j).getTextContent();
                        categories[j] = category;
                    }
                    String author = elem.getElementsByTagName("dc:creator").item(0).getTextContent();
                    // Author
                    Author currentAuthor = new Author();
                    currentAuthor.setName(author);
                    // Post
                    Post currentPost = new Post();
                    currentPost.setArticleType("article");
                    currentPost.setArticleLink(link);
                    currentPost.setArticleTitle(title);
                    currentPost.setAuthor(currentAuthor);
                    currentPost.setArticleSummary(description);
                    currentPost.setAssociatedTags(Arrays.asList(categories));
                    currentPostList.add(currentPost);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentPostList;
    }
}
