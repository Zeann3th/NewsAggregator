package newsaggregator.dataaccess;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import newsaggregator.dataaccess.util.SpellChecker;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
    public static void read(String key, String value) throws IOException {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("WebData");
            MongoCollection<Document> articlesCollection = sampleTrainingDB.getCollection("articles");

            // find one document with new Document
            SpellChecker sp = new SpellChecker();
            sp.check(value);
            List<Document> articles = articlesCollection.find(new Document(key, value)).into(new ArrayList<>());
            for (Document article : articles) {
                System.out.println(article.toJson());
            }
        }
    }
    public static void main(String[] args) throws IOException{ // Example
        read("article_title", "FDIC official urges better digital asset policy to maintain US influence");
    }
}
