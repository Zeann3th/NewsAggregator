package webcrawling;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import posts.Author;
import posts.Post;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BlockchainNewsCrawler extends Crawler {
    @Override
    public void crawl() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // EAGER sẽ chỉ quan tâm đến những thành phần html của trang web nên hoạt động nhanh hơn NORMAL.
        // NORMAL sẽ đợi cho cả trang web load hết, rất lâu...

        WebDriver driver = new EdgeDriver(edgeOptions);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://blockchain.news/");
        WebElement loadMoreButton = driver.findElement(By.xpath("//*[@id=\"btnLoadMore\"]"));
        for (int i = 0; i <= 10; i++) {
            try {
                Thread.sleep(5000); // milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadMoreButton.click();
        }
        List<WebElement> articles = driver.findElements(By.cssSelector(".entry-title > a"));
        List<String> articleLinks = new ArrayList<>();
        for (WebElement article : articles) {
            articleLinks.add(article.getAttribute("href"));
        }
        System.out.println("Tìm thấy " + articles.toArray().length + " kết quả!!");
        List<Post> postList = new ArrayList<>();
        for (String articleLink : articleLinks) {
            driver.get(articleLink);
            // Author hay tác giả
            Author author_i = new Author();
            driver.get(driver.findElement(By.className("entry-cat")).getAttribute("href"));
            author_i.setName(driver.findElement(By.tagName("h1")).getText());
            author_i.setLastPost(driver.findElement(By.cssSelector(".thumbnail-attachment.profile-post-image > a")).getAttribute("href"));
            driver.navigate().back();
            // Thuộc tính bài viết
            Post post_i = new Post();
            post_i.setArticleTitle(driver.findElement(By.cssSelector(".title > b")).getText());
            post_i.setAuthor(author_i);
            post_i.setCreationDate(driver.findElement(By.className("entry-date")).getAttribute("datetime"));
            post_i.setArticleLink(articleLink);
            post_i.setWebsiteSource("Blockchain News");
            post_i.setArticleType("Article");
            post_i.setArticleSummary(driver.findElement(By.className("text-size-big")).getText());
            post_i.setArticleDetailedContent(driver.findElement(By.className("textbody")).getText());
            post_i.setCategory(driver.findElement(By.className("entry-label")).getText());
            postList.add(post_i);
            post_i.display();
            driver.navigate().back();
        }
        driver.quit();
    }
}
