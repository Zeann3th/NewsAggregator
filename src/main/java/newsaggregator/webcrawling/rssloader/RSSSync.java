package newsaggregator.webcrawling.rssloader;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.net.*;
import java.net.http.*;
import java.util.Arrays;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class RSSSync {
    /**
     * Class 
     * Lưu ý: về mặt lý thuyết, phần kết nối HTTP sẽ trả về 304 nếu cache vẫn hợp lệ, nhưng hiện tại chưa thể thực hiện được
     * do HttpURLConnection luôn trả về 200 trong trường hợp này
     *
     * @author: Trần Quang Hưng
     */

    public static void getNewUpdate(String urlString, String cacheURIString){
        /**
         *
         * Phương thức này sẽ tải file RSS nằm tại địa chỉ trong urlString, cập nhật cache nếu có bản cập nhật mới
         * và lưu trữ vào file cache có địa chỉ được truyền vào thông qua cacheURIString
         * Phương pháp lấy tin:
         * - Tạo một kết nối HTTP đến địa chỉ URL của RSS
         * - Tạo một request GET, thêm header "If-Modified-Since" và "Last-Modified" với giá trị là thời gian hiện tại
         * - Nếu response code là 200, tạo một file cache mới và lưu trữ dữ liệu từ URL vào file cache này
         *      - Nếu response code là 304, không làm gì cả
         *      - Sau khi kết nối thành công, tạo một FileOutputStream và InputStream để lưu trữ dữ liệu từ URL vào file cache
         *      - Sử dụng buffer để lưu trữ dữ liệu từ InputStream và ghi vào FileOutputStream
         *      - Đóng FileOutputStream
         *
         * @param urlString: địa chỉ URL của RSS, được cất trong file webSources.txt
         * @param cacheURIString: địa chỉ file cache được lưu trữ dưới dạng "src/main/resources/RSSData/tmp-cache/%s.xml".formatted(domainString)
         */
        try {
            URL url = URI.create(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss");
            String formattedDate = now.format(formatter);
            System.out.println(formattedDate);

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("If-Modified-Since", formattedDate)
                    .header("Last-Modified", formattedDate)
                    .uri(URI.create(urlString))
                    .build();
            connection.setRequestMethod(request.method());
            connection.connect();
            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);
            if (responseCode == 200) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(cacheURIString);
                    InputStream iStream = url.openStream();
                    byte buffer[] = new byte[1024];
                    int length;
                    while ((length = iStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
