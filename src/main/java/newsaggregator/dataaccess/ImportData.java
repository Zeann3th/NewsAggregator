package newsaggregator.dataaccess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Lớp này dùng để nhập dữ liệu từ file JSON/CSV vào database.
 * <br> Hiện tại chỉ hỗ trợ nhập dữ liệu từ file JSON. Nhóm chúng tôi dự định sẽ hỗ trợ nhập dữ liệu từ file CSV trong tương lai.
 * @author Lý Hiển Long
 * @version 1.0
 */
public class ImportData {
    /**
     * Phương thức này dùng để nhập dữ liệu từ file JSON vào database.
     * @param filePath Đường dẫn tới file JSON. e.g: "src/main/resources/data.json"
     * @throws IOException Ném ra nếu có lỗi khi đọc file.
     */
    public static void importJSON(String filePath) throws IOException {
        // Nối tới database
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase db = mongoClient.getDatabase("WebData");
            MongoCollection<Document> collection = db.getCollection("articles");
            File jsonFile = new File(filePath);
            ObjectMapper mapper = new ObjectMapper();
            List<Document> documents = mapper.readValue(jsonFile, new TypeReference<>() {});
            int count = 0;
            for (Document doc : documents) {
                try (MongoCursor<Document> cursor = collection.find(new Document("guid", doc.getString("guid"))).iterator()) {
                    if (cursor.hasNext()) {
                        System.out.println("Bài viết đã tồn tại trong database...");
                    } else {
                        collection.insertOne(doc);
                        count++;
                    }
                } catch (MongoException e) {
                    System.out.println("Lỗi khi đẩy dữ liệu lên database...");
                    e.printStackTrace();
                }
            }
            System.out.println("Đã đẩy " + count + " bài báo lên database...");
        }
    }

    public static void createSearchIndex() {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase database = mongoClient.getDatabase("WebData");
            MongoCollection<Document> collection = database.getCollection("articles");
            Document index = new Document("mappings",
                    new Document("dynamic", true));
            collection.createSearchIndex("searchArticles", index);
            }
    }
}
