package newsaggregator.webcrawling.dynamic;

import newsaggregator.webcrawling.Crawler;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import newsaggregator.posts.Author;
import newsaggregator.posts.Post;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này dùng để thu thập dữ liệu từ trang web Blockchain News.
 * <br> Lớp này kế thừa từ lớp Crawler.
 * @see Crawler
 * @author Lý Hiển Long
 * @since 1.0
 */
public class BlockchainNewsCrawler extends Crawler {
    @Override @Deprecated
    public void crawl() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // EAGER sẽ chỉ quan tâm đến những thành phần html của trang web nên hoạt động nhanh hơn NORMAL.
        // NORMAL sẽ đợi cho cả trang web load hết, rất lâu...

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://blockchain.news/");
        for (int i = 0; i <= 10; i++) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement loadMoreButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"btnLoadMore\"]")));
            loadMoreButton.click();
        }
        List<WebElement> articles = driver.findElements(By.cssSelector(".entry-title a"));
        List<String> articleLinks = new ArrayList<>();
        for (WebElement article : articles) {
            articleLinks.add(article.getAttribute("href"));
        }
        System.out.println("Tìm thấy " + articles.toArray().length + " kết quả!!");
        List<Post> postList = new ArrayList<>();
        for (String articleLink : articleLinks) {
            driver.get(articleLink);
            // Author hay tác giả
            Author currentAuthor = new Author();
            driver.get(driver.findElement(By.className("entry-cat")).getAttribute("href"));
            currentAuthor.setName(driver.findElement(By.tagName("h1")).getText());
            currentAuthor.setLastPost(driver.findElement(By.cssSelector(".thumbnail-attachment.profile-post-image a")).getAttribute("href"));
            driver.navigate().back();
            // Thuộc tính bài viết
            Post currentPost = new Post();
            currentPost.setArticleTitle(driver.findElement(By.cssSelector(".title b")).getText());
            currentPost.setAuthor(currentAuthor);
            currentPost.setCreationDate(driver.findElement(By.className("entry-date")).getAttribute("datetime"));
            currentPost.setArticleLink(articleLink);
            currentPost.setWebsiteSource("Blockchain News");
            currentPost.setArticleType("Article");
            currentPost.setArticleSummary(driver.findElement(By.className("text-size-big")).getText());
            currentPost.setArticleDetailedContent(driver.findElement(By.className("textbody")).getText());
            currentPost.setCategory(driver.findElement(By.className("entry-label")).getText());
            postList.add(currentPost);
            driver.navigate().back();
        }
        driver.quit();
        setPostList(postList);
    }
}
