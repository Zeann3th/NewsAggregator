package newsaggregator.webcrawling.dynamic;

import newsaggregator.post.Author;
import newsaggregator.post.Post;
import newsaggregator.webcrawling.Crawler;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        try {
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("onetrust-banner-sdk"))));
            driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        } catch (Exception e) {
            System.out.println("Không tìm thấy banner cookies");
        }

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
                currentPost.setCreationDate(timeConversion(time));
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
                currentPost.display();
                postList.add(currentPost);
            } catch (Exception e) {
                System.out.println("Không thể crawl bài viết này");
                e.printStackTrace();
            }
        }
        driver.quit();
        setPostList(postList);
    }
    @NotNull
    private String timeConversion(String time) {
        String[] timeSplit = time.substring(time.indexOf("•")+2).replaceAll(",", "").split(" ");
        String month = timeSplit[0];
        int monthInt = 0;
        switch (month) {
            case "January":
                monthInt = 1;
                break;
            case "February":
                monthInt = 2;
                break;
            case "March":
                monthInt = 3;
                break;
            case "April":
                monthInt = 4;
                break;
            case "May":
                monthInt = 5;
                break;
            case "June":
                monthInt = 6;
                break;
            case "July":
                monthInt = 7;
                break;
            case "August":
                monthInt = 8;
                break;
            case "September":
                monthInt = 9;
                break;
            case "October":
                monthInt = 10;
                break;
            case "November":
                monthInt = 11;
                break;
            case "December":
                monthInt = 12;
                break;
        }
        String timePart = timeSplit[3];
        int hour;
        if (timePart.endsWith("PM")) {
            hour = Integer.parseInt(timePart.substring(0, timePart.indexOf(":"))) + 12;
        }
        else {
            hour = Integer.parseInt(timePart.substring(0, timePart.indexOf(":")));
        }
        // Create LocalDateTime object with time zone
        ZonedDateTime zdt = ZonedDateTime.of(
                Integer.parseInt(timeSplit[2]),
                monthInt,
                Integer.parseInt(timeSplit[1]),
                hour,
                Integer.parseInt(timePart.substring(timePart.indexOf(":") + 1, timePart.length()-2)), 0, 0,
                ZoneId.of("America/New_York"));

        // Format using ISO_OFFSET_DATE_TIME
        return zdt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
