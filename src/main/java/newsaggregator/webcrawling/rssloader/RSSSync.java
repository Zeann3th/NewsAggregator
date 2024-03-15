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
    public void getNewUpdate(String urlString, String cacheURIString){
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
                InputStream inputStream = connection.getInputStream();
                Document doc = builder.parse(inputStream);
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
//                finally {
//                    NodeList items = doc.getElementsByTagName("item");
//                    for (int i = 0; i < items.getLength(); i++) {
//                        Node item = items.item(i);
//                        if (item.getNodeType() == Node.ELEMENT_NODE) {
//                            Element elem = (Element) item;
//                            String title = elem.getElementsByTagName("title").item(0).getTextContent();
//                            String link = elem.getElementsByTagName("link").item(0).getTextContent();
//                            System.out.printf("""
//                                Title: %s,
//                                Link: %s
//                                %n""", title, link);
//                        }
//                    }
//                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
