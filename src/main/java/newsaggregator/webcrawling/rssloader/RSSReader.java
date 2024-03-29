package newsaggregator.webcrawling.rssloader;

import newsaggregator.post.Author;
import newsaggregator.post.Post;
import newsaggregator.webcrawling.Crawler;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RSSReader extends Crawler {
    /**
     * Lớp RSSReader thực hiện việc đọc file XML từ các nguồn RSS được lưu trữ trong file webSources.txt
     * và trả về một danh sách các bài báo được lưu trữ trong các file XML này
     * @throws Exception
     * @author: Trần Quang Hưng
     */
    @Override
    public void crawl() {
//        RSSSync rssSync = new RSSSync();
        List<Post> postList = new ArrayList<>();
        try {
            File newsList = new File("src/main/resources/rssdata/webSources.txt");
            Scanner newsListScanner = new Scanner(newsList);
            while (newsListScanner.hasNextLine()) {
                String urlString = newsListScanner.nextLine();
                String domainString = URI.create(urlString).getHost();
                RSSSync.getNewUpdate(urlString, "src/main/resources/rssdata/tmp-cache/%s.xml".formatted(domainString));
                RSSReader rssReader = new RSSReader();
                List<Post> currentPostList = rssReader.parseXML("src/main/resources/rssdata/tmp-cache/%s.xml".formatted(domainString));
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
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(URIString);
            NodeList items = doc.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Node item = items.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) item;
                    String guid = elem.getElementsByTagName("guid").item(0).getTextContent();
                    String title = elem.getElementsByTagName("title").item(0).getTextContent();
                    String link = elem.getElementsByTagName("link").item(0).getTextContent();
                    String date = elem.getElementsByTagName("pubDate").item(0).getTextContent();
                    String summary = Jsoup.parse(elem.getElementsByTagName("description").item(0).getTextContent()).text();
                    try {
                        String thumbnail = null;
                        try {
                            List<String> thumbnailTagList = new ArrayList<>(Arrays.asList("media:thumbnail", "media:content", "enclosure"));
                            for (String thumbnailTag : thumbnailTagList) {
                                thumbnail = elem.getElementsByTagName(thumbnailTag).item(0).getAttributes().getNamedItem("url").getTextContent();
                                if (thumbnail != null) {
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            continue;
                        }
                        String detailedContent = null;
                        try {
                            detailedContent = Jsoup.parse(elem.getElementsByTagName("content:encoded").item(0).getTextContent()).text();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String[] categories = new String[elem.getElementsByTagName("category").getLength()];
                        for (int j = 0; j < elem.getElementsByTagName("category").getLength(); j++) {
                            String category = elem.getElementsByTagName("category").item(j).getTextContent();
                            categories[j] = category;
                        }
                        String author = elem.getElementsByTagName("dc:creator").item(0).getTextContent();

                        // Author
                        Author currentAuthor = new Author(author);

                        // Post
                        Post currentPost = new Post(
                                guid,
                                link,
                                URIString.replace(".xml", "")
                                        .replace("src/main/resources/RSSData/tmp-cache/", "")
                                        .replace("www.", "")
                                        .replace(".com", ""),
                                "article",
                                title,
                                summary,
                                detailedContent,
                                outputFormat.format(inputFormat.parse(date)),
                                Arrays.asList(categories),
                                currentAuthor,
                                thumbnail,
                                null
                        );

                        currentPostList.add(currentPost);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentPostList;
    }
}
