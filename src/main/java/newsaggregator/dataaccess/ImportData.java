package newsaggregator.dataaccess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImportData {
    public void importJSON(String filePath) throws IOException {
        // Nối tới database
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))){
            MongoDatabase db = mongoClient.getDatabase("WebData");
            MongoCollection<Document> collection = db.getCollection("articles");
            collection.drop(); // Xoá trước khi nhập vào phòng trường hợp duplicate
            File jsonFile = new File(filePath);
            ObjectMapper mapper = new ObjectMapper();
            List<Document> documents = mapper.readValue(jsonFile, new TypeReference<>() {});
            for (Document doc : documents) {
                collection.insertOne(doc);
            }
            System.out.println("Đã đẩy lên database...");
        }
    }
    public void importCSV() {}
}
