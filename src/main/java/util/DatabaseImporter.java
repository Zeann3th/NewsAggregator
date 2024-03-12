package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DatabaseImporter {
    public static void main(String[] args) throws IOException {
        // Nối tới database
        String uri = "<<connectionString here>";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase db = mongoClient.getDatabase("WebData");
        MongoCollection<Document> collection = db.getCollection("articles");
        try {
            collection.drop(); // Xoá trước khi nhập vào phòng trường hợp duplicate
            File jsonFile = new File("src/main/resources/data/data.json");
            ObjectMapper mapper = new ObjectMapper();
            List<Document> documents = mapper.readValue(jsonFile, new TypeReference<List<Document>>() {});

            for (Document doc : documents) {
                collection.insertOne(doc);
            }
            System.out.println("Đã đẩy lên database...");
        }
        catch (MongoException e) {
            e.printStackTrace();
        }

    }
}
