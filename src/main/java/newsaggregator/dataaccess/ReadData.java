package newsaggregator.dataaccess;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Class
 * Lớp này dùng để thực hiện các thao tác tìm kiếm trên database.
 * @author Lý Hiển Long
 */
public class ReadData {
    private List<Document> queryData;

    public List<Document> getQueryData() {
        return queryData;
    }

    public void setQueryData(List<Document> queryData) {
        this.queryData = queryData;
    }

    /**
     * Phương thức này dùng để đọc dữ liệu từ database.
     * @param value Nội dung cần tìm kiếm
     * @param sortOrder Sắp xếp theo thứ tự mới nhất (asc) hoặc cũ nhất (desc)
     * e.g: QueryData qd = new QueryData();
     *      qd.read("dao", "desc");
     *      -> Tìm kiếm tất cả các bài viết, các trường trong bài viết có chứa từ "dao" và sắp xếp theo thứ tự mới nhất.
     * TODO: Tìm kiếm trường cụ thể, ...
     */
    public void search(String value, String sortOrder) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase db = mongoClient.getDatabase("WebData");
            MongoCollection<Document> articlesCollection = db.getCollection("articles");
            List<Bson> search = Arrays.asList(
                    new Document("$search",
                            new Document("index", "searchArticles")
                                    .append("text",
                                            new Document("query", value)
                                                    .append("path",
                                                            new Document("wildcard", "*"))
                                                    .append("fuzzy", new Document()))),
                    new Document("$sort",
                            new Document("creation_date", "desc".equals(sortOrder) ? -1L : 1L)));
            List<Document> articles = articlesCollection.aggregate(search).into(new ArrayList<>());
            for (Document article : articles) {
                System.out.println(article.toJson());
            }
            setQueryData(articles);
        }
    }

    /**
     * Phương thức này dùng để gợi ý người dùng khi nhập từ khóa tìm kiếm.
     * Cần được đặt vào trong Thread khi chạy trên ứng dụng.
     * @param value Nội dung cần tìm kiếm.
     */
    public void autoComplete(String value) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase db = mongoClient.getDatabase("WebData");
            MongoCollection<Document> articlesCollection = db.getCollection("articles");
            List<Bson> search = Arrays.asList(
                    new Document("$search",
                            new Document("index", "titleAutocomplete")
                                    .append("autocomplete",
                                            new Document("query", value)
                                                    .append("path", "article_title")
                                                    .append("tokenOrder", "sequential"))),
                    new Document("$project", new Document("article_title", 1)));
            List<Document> articles = articlesCollection.aggregate(search).into(new ArrayList<>());
            for (Document article : articles) {
                System.out.println(article.toJson());
            }
            setQueryData(articles);
        }
    }
}
