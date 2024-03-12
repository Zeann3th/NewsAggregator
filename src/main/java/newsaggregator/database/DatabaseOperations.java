package newsaggregator.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DatabaseOperations {
    private String filePath;
    private String articleType;
    public DatabaseOperations(String filePath, String articleType) {
        this.filePath = filePath;
        this.articleType = articleType;
    }
    public void importJSON(String filePath) throws IOException{
        // Nối tới database
        String uri = "mongodb+srv://<username>:<password>@cluster0.lsmwbun.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"; // WriteUser
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase db = mongoClient.getDatabase("WebData");
        MongoCollection<Document> collection = db.getCollection(articleType);
        try {
            collection.drop(); // Xoá trước khi nhập vào phòng trường hợp duplicate
            File jsonFile = new File(filePath);
            ObjectMapper mapper = new ObjectMapper();
            List<Document> documents = mapper.readValue(jsonFile, new TypeReference<>() {});
            for (Document doc : documents) {
                collection.insertOne(doc);
            }
            System.out.println("Đã đẩy lên database...");
        }
        catch (MongoException e) {
            e.printStackTrace();
        }
    }
    public void query(List<String> keyNValue, String order) {
        String uri = "mongodb+srv://<username>:<password>@cluster0.lsmwbun.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"; // QueryUser
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase db = mongoClient.getDatabase("WebData");
        MongoCollection<Document> collection = db.getCollection(articleType);
    }
}