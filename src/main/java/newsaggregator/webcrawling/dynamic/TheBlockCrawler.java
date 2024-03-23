package newsaggregator.webcrawling.dynamic;

import newsaggregator.posts.Author;
import newsaggregator.posts.Post;
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
        edgeOptions.addArguments("--disable-notifications", "start-maximized", "--disable-popup-blocking");
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.theblock.co/latest");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("onetrust-banner-sdk"))));
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        List<String> articleLinks = new ArrayList<>();
        final String baseUrl = "https://www.theblock.co/latest?start=";
        int totalArticles = 0;
        for (int i = 0; i < 10; i++) {
            String url = baseUrl + (i * 10);
            List<WebElement> articles = driver.findElements(By.cssSelector(".collection__feed > .articles > article > div > a"));
            totalArticles += articles.size();
            for (WebElement article : articles) {
                articleLinks.add(article.getAttribute("href"));
            }
        }
        System.out.println("Tìm thấy " + totalArticles + " kết quả!!");
        System.out.println("Lưu ý: Số bài báo có thể khác do một số bài không thể crawl được nội dung");
        List<Post> postList = new java.util.ArrayList<>();
        for (String articleLink : articleLinks) {
            driver.get(articleLink);
            Post currentPost = new Post();
            try {
                Author currentAuthor = new Author();
                driver.get(driver.findElement(By.cssSelector(".articleByline a")).getAttribute("href"));
                currentAuthor.setName(driver.findElement(By.cssSelector(".titleInfo > h1")).getText());
                currentAuthor.setDescription(driver.findElement(By.className("description")).getText());
                driver.navigate().back();
                wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".articleBody > h1"))));
                currentPost.setArticleTitle(driver.findElement(By.cssSelector(".articleBody > h1")).getText());
                currentPost.setAuthor(currentAuthor);
                String time = driver.findElement(By.cssSelector(".ArticleTimestamps > div")).getText();
                time = time.substring(time.indexOf("•")+2);
                currentPost.setCreationDate(time);
                currentPost.setArticleLink(articleLink);
                currentPost.setWebsiteSource("The Block");
                currentPost.setArticleType("article");
                currentPost.setArticleSummary(driver.findElement(By.className("quickTake")).getText());
                currentPost.setArticleDetailedContent(driver.findElement(By.cssSelector("#articleContent > span")).getText()
                        .replace("Disclaimer: The Block is an independent media outlet that delivers news, research, and data. As of November 2023, Foresight Ventures is a majority investor of The Block. Foresight Ventures invests in other companies in the crypto space. Crypto exchange Bitget is an anchor LP for Foresight Ventures. The Block continues to operate independently to deliver objective, impactful, and timely information about the crypto industry. Here are our current financial disclosures.\n" +
                        "© 2023 The Block. All Rights Reserved. This article is provided for informational purposes only. It is not offered or intended to be used as legal, tax, investment, financial, or other advice.", "")
                        .replace(driver.findElement(By.cssSelector("#articleContent > span > section")).getText(), "")
                        .trim());
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
        }
        driver.quit();
        setPostList(postList);
    }
}
