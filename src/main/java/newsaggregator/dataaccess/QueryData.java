package newsaggregator.dataaccess;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.search;
import static com.mongodb.client.model.Indexes.text;

/**
 * Lớp này dùng để thực hiện thao tác tìm kiếm liệu trên database.
 */
public class QueryData {
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
     * TODO: Cần thêm chức năng AutoComplete, Tìm kiếm trường cụ thể, ...
     */
    public void read(String value, String sortOrder) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase db = mongoClient.getDatabase("WebData");
            MongoCollection<Document> articlesCollection = db.getCollection("articles");
//            SpellChecker sp = new SpellChecker();
//            sp.check(value);
            List<Bson> search = Arrays.asList(new Document("$search",
                            new Document("index", "searchArticles")
                                    .append("text",
                                            new Document("query", "dao")
                                                    .append("path",
                                                            new Document("wildcard", "*")))),
                    new Document("$addFields",
                            new Document("converted_creation_date",
                                    new Document("$dateFromString",
                                            new Document("dateString", "$creation_date")
                                                    .append("onError", "$creation_date")))),
                    new Document("$sort",
                            new Document("converted_creation_date", "desc".equals(sortOrder) ? -1L : 1L)));
            List<Document> articles = articlesCollection.aggregate(search).into(new ArrayList<>());
            for (Document article : articles) {
                System.out.println(article.toJson());
            }
            setQueryData(articles);
        }
    }
}
