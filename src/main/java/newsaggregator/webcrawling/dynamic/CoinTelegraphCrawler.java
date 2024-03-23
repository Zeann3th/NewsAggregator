package newsaggregator.webcrawling.dynamic;

import newsaggregator.webcrawling.Crawler;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import newsaggregator.posts.Author;
import newsaggregator.posts.Post;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này dùng để thu thập dữ liệu từ trang web Coin Telegraph.
 * <br> Lớp này kế thừa từ lớp Crawler.
 * @see Crawler
 * @since 1.0
 * @author Lý Hiển Long
 */
public class CoinTelegraphCrawler extends Crawler {
    @Override
    public void crawl() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--disable-notifications", "start-maximized", "--disable-popup-blocking");
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // EAGER sẽ chỉ quan tâm đến những thành phần html của trang web nên hoạt động nhanh hơn NORMAL.
        // NORMAL sẽ đợi cho cả trang web load hết, rất lâu...

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://cointelegraph.com/tags/blockchain");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        for (int i = 0; i <= 10; i++) { // Lấy ra tầm 60 bài báo
            // "Lăn chuột" để xuống cuối trang
            Actions actions = new Actions(driver);
            actions.moveToElement(driver.findElement(By.cssSelector(".btn.posts-listing__more-btn"))).perform();
            try {
                Thread.sleep(6000); // milliseconds
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
        System.out.println("Lưu ý: Số bài báo có thể khác do một số bài không thể crawl được nội dung");
        // Lấy dữ liệu ở từng bài báo
        List<Post> postList = new ArrayList<>();
        for (String articleLink : articleLinks) {
            driver.get(articleLink);
            Post currentPost = new Post();
            // Author hay tác giả
            try {
                Author currentAuthor = new Author();
                driver.get(driver.findElement(By.className("post-meta__author-link")).getAttribute("href"));
                currentAuthor.setName(driver.findElement(By.className("author-about__header")).getText());
                currentAuthor.setDescription(driver.findElement(By.className("author-about__desc")).getText());
                driver.navigate().back();
                // Thuộc tính bài viết
                currentPost.setArticleTitle(driver.findElement(By.tagName("h1")).getText());
                currentPost.setAuthor(currentAuthor);
                currentPost.setCreationDate(driver.findElement(By.tagName("time")).getAttribute("datetime"));
                currentPost.setArticleLink(articleLink);
                currentPost.setWebsiteSource("Coin Telegraph");
                currentPost.setArticleType("Article");
                currentPost.setArticleSummary(driver.findElement(By.className("post__lead")).getText());
                currentPost.setArticleDetailedContent(driver.findElement(By.cssSelector(".post-content.relative")).getText());
                currentPost.setCategory(driver.findElement(By.className("post-cover__badge")).getText());
                List<WebElement> articleTags = driver.findElements(By.className("tags-list__link"));
                List<String> tags = new ArrayList<>();
                for (WebElement articleTag : articleTags) {
                    tags.add(articleTag.getText());
                }
                currentPost.setAssociatedTags(tags);
            } catch (Exception e) {
                continue;
            }
            postList.add(currentPost);
//            currentPost.display();
            driver.navigate().back();
        }
        driver.quit();
        setPostList(postList);
    }
}