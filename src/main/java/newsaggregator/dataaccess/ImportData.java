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
 * Class
 * Lớp này dùng để nhập dữ liệu từ file JSON vào database và tạo index cho các bài báo.
 * @author Lý Hiển Long
 */
public class ImportData {
    /**
     * Phương thức này sẽ kết nối tới database của MongoDB và đẩy dữ liệu từ file JSON lên database thông qua thư viện MongoDB-driver-sync.
     * Bài viết sẽ được đẩy lên database `WebData`, collection `articles`.
     * Nếu bài viết đã tồn tại trong database, nó sẽ không đẩy lên nữa.
     * @param filePath Đường dẫn tới file JSON. e.g: "src/main/resources/data/upstream/data.json"
     */
    public static void importJsonToDatabase(String filePath) throws IOException {
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

    /**
     * Phương thức này sẽ tạo index cho các bài báo trong database nhằm phục vụ cho full-text search.
     * Index sẽ được tạo ra cho mọi trường thuộc bài báo trong collection `articles`.
     * Phương pháp tạo index:
     * - Tạo kết nối HTTP đến MongoDB Atlas với API key được mã hóa bằng phương thức Digest.
     * - Tạo một request POST với body là thông tin về index cần tạo (tên database, collection, tên index, lựa chọn full-text search).
     * - Nếu response code là 200, index đã được tạo thành công.
     * - Nếu response code là 403, đã có index với tên tương tự trong collection => bad request (không đáng lo ngại).
     */
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
