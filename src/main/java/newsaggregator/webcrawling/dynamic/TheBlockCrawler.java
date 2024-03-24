package newsaggregator.webcrawling.dynamic;

import newsaggregator.post.Author;
import newsaggregator.post.Post;
import newsaggregator.webcrawling.Crawler;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class TheBlockCrawler extends Crawler {
    @Override
    public void crawl() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--disable-notifications", "start-maximized", "--disable-popup-blocking", "--headless=new");
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.theblock.co/latest");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Accept cookies
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("onetrust-banner-sdk"))));
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();

        List<String> articleLinks = new ArrayList<>();
        final String baseUrl = "https://www.theblock.co/latest?start=";
        int totalArticles = 0;
        for (int i = 0; i < 30; i++) {
            String url = baseUrl + (i * 10);
            List<WebElement> articles = driver.findElements(By.cssSelector(".collection__feed > .articles > article > div > a"));
            totalArticles += articles.size();
            for (WebElement article : articles) {
                articleLinks.add(article.getAttribute("href"));
            }
        }

        System.out.println("Đang crawl dữ liệu từ The Block...");
        System.out.println("Tìm thấy " + totalArticles + " kết quả!!");
        System.out.println("Lưu ý: Số bài báo có thể khác do một số bài không thể crawl được nội dung");

        List<Post> postList = new java.util.ArrayList<>();
        for (String articleLink : articleLinks) {
            driver.get(articleLink);
            Post currentPost = new Post();
            try {
                // Tác giả bài viết
                Author currentAuthor = new Author();
                driver.get(driver.findElement(By.cssSelector(".articleByline a")).getAttribute("href"));
                currentAuthor.setName(driver.findElement(By.cssSelector(".titleInfo > h1")).getText());
                currentAuthor.setDescription(driver.findElement(By.className("description")).getText());
                driver.navigate().back();
                // Thông tin bài viết
                wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".articleBody > h1"))));
                currentPost.setArticleTitle(driver.findElement(By.cssSelector(".articleBody > h1")).getText());
                currentPost.setAuthor(currentAuthor);
                String time = driver.findElement(By.cssSelector(".ArticleTimestamps > div")).getText();
                time = time.substring(time.indexOf("•")+2);
                currentPost.setCreationDate(time);
                currentPost.setArticleLink(articleLink);
                currentPost.setWebsiteSource("The Block");
                currentPost.setArticleType("article");
                currentPost.setThumbnailImage(driver.findElement(By.cssSelector(".articleContent > article > .articleFeatureImage.type\\:primaryImage > img")).getAttribute("src"));
                currentPost.setArticleSummary(driver.findElement(By.className("quickTake")).getText());
                String articleDetailedContent = driver.findElement(By.cssSelector("#articleContent > span")).getText();
                articleDetailedContent.replace(driver.findElement(By.cssSelector("#articleContent > span > span.copyright")).getText(), "");
                try {
                    articleDetailedContent.replace(driver.findElement(By.className("articleCharts")).getText(), "");
                } catch (Exception e) {
                    System.out.println("Không có biểu đồ trong bài viết");
                }
                articleDetailedContent.trim();
                currentPost.setArticleDetailedContent(articleDetailedContent);
                currentPost.setCategory("NEWS");
                List<WebElement> articleTags = driver.findElements(By.className("tag"));
                List<String> tags = new java.util.ArrayList<>();
                for (WebElement articleTag : articleTags) {
                    tags.add(articleTag.getText());
                }
                currentPost.setAssociatedTags(tags);
                postList.add(currentPost);
            } catch (Exception e) {
                System.out.println("Không thể crawl bài viết này");
                e.printStackTrace();
            }
        }
        driver.quit();
        setPostList(postList);
    }
}
