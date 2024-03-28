package newsaggregator.dataaccess;

import com.burgstaller.okhttp.AuthenticationCacheInterceptor;
import com.burgstaller.okhttp.CachingAuthenticatorDecorator;
import com.burgstaller.okhttp.digest.CachingAuthenticator;
import com.burgstaller.okhttp.digest.Credentials;
import com.burgstaller.okhttp.digest.DigestAuthenticator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import okhttp3.*;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
//            collection.drop();
            List<Document> documents = mapper.readValue(jsonFile, new TypeReference<>() {});
            int count = 0;
            for (Document doc : documents) {
                try (MongoCursor<Document> cursor = collection.find(new Document("guid", doc.getString("guid"))).iterator()) {
                    if (cursor.hasNext()) {
//                        System.out.println("Bài viết đã tồn tại trong database...");
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
        final DigestAuthenticator authenticator =
                new DigestAuthenticator(
                        new Credentials("<PUBLIC_API_KEY>", "<PRIVATE_API_KEY>"));
        final Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();
        OkHttpClient client = new OkHttpClient.Builder()
                .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
                .addInterceptor(new AuthenticationCacheInterceptor(authCache))
                .build();
        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .url("https://cloud.mongodb.com/api/atlas/v1.0/groups/<GROUP_ID>/clusters/<CLUSTER_NAME>/fts/indexes?pretty=true")
                .post(RequestBody.create(
                        "{\"collectionName\": \"articles\", " +
                                "\"database\": \"WebData\", " +
                                "\"mappings\": {\"dynamic\": true}, " +
                                "\"name\": \"searchArticles\"}",
                        MediaType.parse("application/json")))
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Status code: " + response.code());
            System.out.println("Response body: " + response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
