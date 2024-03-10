package webcrawling;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import posts.Post;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CoinTelegraphCrawler extends Crawler {
    public void crawl() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // EAGER sẽ chỉ quan tâm đến những thành phần html của trang web nên hoạt động nhanh hơn NORMAL.
        // NORMAL sẽ đợi cho cả trang web load hết, rất lâu...

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.get("https://cointelegraph.com/tags/blockchain");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        for (int i = 0; i <= 3; i++) { // Lấy ra tầm 60 bài báo
            // "Lăn chuột" để xuống cuối trang
            jse.executeScript("window.scrollBy(0,10000)", "");
            // Đợi 10 giây để trang load các bài báo tiếp theo
            try {
                Thread.sleep(2500); // milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<WebElement> articles = driver.findElements(By.className("post-card-inline__figure-link"));
        List<String> articleLinks = new ArrayList<>();
        // Vì một lí do nào đó mà không thể làm 1 vòng for để vào từng trang được... Khá là cồng kềnh
        for (WebElement article : articles) {
            articleLinks.add(article.getAttribute("href"));
            System.out.println(article.getAttribute("href"));
        }
        // Lấy dữ liệu ở từng bài báo
        for (String articleLink : articleLinks) {
            driver.get(articleLink);
            String title = driver.findElement(By.className("text-headlineXxxl.text-29.md:text-41")).getText();
            System.out.println(title);
            driver.navigate().back();
        }
        driver.quit();
    }

    public static void main(String[] args) {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // EAGER sẽ chỉ quan tâm đến những thành phần html của trang web nên hoạt động nhanh hơn NORMAL.
        // NORMAL sẽ đợi cho cả trang web load hết, rất lâu...

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://cointelegraph.com/tags/blockchain");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        for (int i = 0; i <= 3; i++) { // Lấy ra tầm 60 bài báo
            // "Lăn chuột" để xuống cuối trang
            jse.executeScript("window.scrollBy(0,10000)", "");
            // Đợi 10 giây để trang load các bài báo tiếp theo
            try {
                Thread.sleep(2500); // milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<WebElement> articles = driver.findElements(By.className("post-card-inline__figure-link"));
        List<String> articleLinks = new ArrayList<>();
        // Vì một lí do nào đó mà không thể làm 1 vòng for để vào từng trang được... Khá là cồng kềnh
        for (WebElement article : articles) {
            articleLinks.add(article.getAttribute("href"));
        }
        System.out.println("Tìm thấy " + articles.toArray().length + " kết quả!!");
        // Lấy dữ liệu ở từng bài báo
        List<Post> postList = new ArrayList<>();
        for (String articleLink : articleLinks) {
            driver.get(articleLink);
            Post post_i = new Post();
            post_i.setArticleTitle(driver.findElement(By.tagName("h1")).getText());
            post_i.setCreationDate(driver.findElement(By.tagName("time")).getAttribute("datetime"));
            post_i.setArticleLink(articleLink);
            post_i.setWebsiteSource("Coin Telegraph");
            post_i.display();
            driver.navigate().back();
        }
        driver.quit();
    }
}