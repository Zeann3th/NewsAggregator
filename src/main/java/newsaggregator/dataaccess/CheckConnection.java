package newsaggregator.dataaccess;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này dùng để kiểm tra kết nối tới database.
 * @author Long Ly
 * @since  1.0
 */
public class CheckConnection {
    public static void main(String[] args) {
        String connectionString = System.getProperty("mongodb.uri");
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            System.out.println("Kết nối thành công tới database");
            List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
            databases.forEach(db -> System.out.println(db.toJson()));
        }
    }
}
