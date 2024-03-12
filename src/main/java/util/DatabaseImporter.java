package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DatabaseImporter {
    public static void main(String[] args) throws IOException {
        // Nối tới database
        String uri = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase db = mongoClient.getDatabase("WebData");
        MongoCollection<Document> collection = db.getCollection("articles");

        File jsonFile = new File("src/main/resources/data/data.json");
        ObjectMapper mapper = new ObjectMapper();
        List<Document> documents = mapper.readValue(jsonFile, new TypeReference<List<Document>>() {});

        for (Document doc : documents) {
            collection.insertOne(doc);
        }
    }
}
