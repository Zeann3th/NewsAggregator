package webcrawling;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import posts.Post;

import java.util.ArrayList;
import java.util.List;

public class TodayOnChainCrawler extends Crawler{
    public void crawl() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        // EAGER sẽ chỉ quan tâm ến những thành phần html hay JS của trang web nên hoạt động nhanh hơn NORMAL.
        // NORMAL sẽ đợi cho cả trang web load hết, rất lâu...

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.get("https://todayonchain.com/#google_vignette");
        List<WebElement> articles = driver.findElements(By.cssSelector("#api-articles > a"));
    }

    public static void main(String[] args) {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // EAGER sẽ chỉ quan tâm đến những thành phần html của trang web nên hoạt động nhanh hơn NORMAL.
        // NORMAL sẽ đợi cho cả trang web load hết, rất lâu...

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.get("https://todayonchain.com/#google_vignette");
        List<WebElement> articles = driver.findElements(By.cssSelector("#api-articles > a"));
        List<String> articleLinks = new ArrayList<>();
        for (WebElement article : articles) {
            articleLinks.add(article.getAttribute("href"));
        }
        System.out.println("Tìm thấy " + articles.toArray().length + " kết quả!!");
        // Lấy dữ liệu ở từng bài báo
        List<Post> postlist = new ArrayList<>();
        for (String articleLink : articleLinks) {
            driver.get(articleLink);

            driver.navigate().back();
        }
    }
}
