package newsaggregator.webcrawling.dynamic;

import newsaggregator.posts.Author;
import newsaggregator.posts.Post;
import newsaggregator.webcrawling.Crawler;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TheBlockCrawler extends Crawler {
    @Override
    public void crawl() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--disable-notifications", "start-maximized", "--disable-popup-blocking");
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.theblock.co/latest");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("onetrust-banner-sdk"))));
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        WebElement nextButton = driver.findElement(By.cssSelector(".next.active"));
//        for (int i = 0; i <= 10; i++) {
//            nextButton.click();
//            wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
//        }
        List<WebElement> articles = driver.findElements(By.cssSelector(".headline > a"));
        List<String> articleLinks = new java.util.ArrayList<>();
        for (WebElement article : articles) {
            articleLinks.add(article.getAttribute("href"));
        }
        System.out.println("Tìm thấy " + articles.toArray().length + " kết quả!!");
        System.out.println("Lưu ý: Số bài báo có thể khác do một số bài không thể crawl được nội dung");
        List<Post> postList = new java.util.ArrayList<>();
        for (String articleLink : articleLinks) {
            driver.get(articleLink);
            Post currentPost = new Post();
            try {
                Author currentAuthor = new Author();
                driver.get(driver.findElement(By.cssSelector(".articleByline a")).getAttribute("href"));
                currentAuthor.setName(driver.findElement(By.cssSelector(".titleInfo > h1")).getText());
                currentAuthor.setLastPost(driver.findElement(By.cssSelector(".cardContainer>a")).getAttribute("href"));
                driver.navigate().back();

                currentPost.setArticleTitle(driver.findElement(By.cssSelector(".articleBody > h1")).getText());
                currentPost.setAuthor(currentAuthor);
                String time = driver.findElement(By.cssSelector(".ArticleTimestamps > div")).getText();
                time = time.substring(time.indexOf("•")+2);
                currentPost.setCreationDate(time);
                currentPost.setArticleLink(articleLink);
                currentPost.setWebsiteSource("The Block");
                currentPost.setArticleType("article");
                currentPost.setArticleSummary(driver.findElement(By.className("quickTake")).getText());
                currentPost.setArticleDetailedContent(driver.findElement(By.cssSelector("#articleContent > span")).getText());
                currentPost.setCategory("NEWS");
                List<WebElement> articleTags = driver.findElements(By.className("tag"));
                List<String> tags = new java.util.ArrayList<>();
                for (WebElement articleTag : articleTags) {
                    tags.add(articleTag.getText());
                }
                currentPost.setAssociatedTags(tags);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            postList.add(currentPost);
            currentPost.display();
            driver.navigate().back();
        }
        driver.quit();
        setPostList(postList);
    }
}
