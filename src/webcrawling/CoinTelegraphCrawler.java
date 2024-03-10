package webcrawling;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.ArrayList;
import java.util.List;

public class CoinTelegraphCrawler extends Crawler {
    public void crawl() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // EAGER sẽ chỉ quan tâm ến những thành phần html hay JS của trang web nên hoạt động nhanh hơn NORMAL.
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
}