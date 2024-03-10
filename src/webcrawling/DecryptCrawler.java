package webcrawling;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import posts.Author;
import posts.Post;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DecryptCrawler extends Crawler {
    @Override
    public void crawl() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        edgeOptions.addArguments("--headless=new");
        // EAGER sẽ chỉ quan tâm đến những thành phần html của trang web nên hoạt động nhanh hơn NORMAL.
        // NORMAL sẽ đợi cho cả trang web load hết, rất lâu...

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://decrypt.co/news");
        WebElement moreButton = driver.findElement(By.className("text-sm xl:text-base font-akzidenz-grotesk text-black mr-2 gg-dark:text-neutral-100 gg-dark:font-poppins degen-alley-dark:text-neutral-100"));
        for (int i = 0; i <= 3; i++) {
            moreButton.click();
        }
    }

    public static void main(String[] args) {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // EAGER sẽ chỉ quan tâm đến những thành phần html của trang web nên hoạt động nhanh hơn NORMAL.
        // NORMAL sẽ đợi cho cả trang web load hết, rất lâu...

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://decrypt.co/news");
        WebElement moreButton = driver.findElement(By.className("mr-4"));
        for (int i = 0; i < 2; i++) {
            moreButton.click();
            try {
                Thread.sleep(2500); // milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<WebElement> articles = driver.findElements(By.cssSelector("#__next > div > div.__variable_4a3e9c.__variable_755843.__variable_4416bf.__variable_95285a.__variable_5091bb.__variable_418661.__variable_e62eb6.__variable_5607af > div > main > div > div:nth-child(6) > div > article > article:nth-child(1) > div.flex.flex-col.border-l-\\[0\\.5px\\].ml-0\\.5.border-neutral-300.pl-2.md\\:pl-3.xl\\:pl-4.pt-7 > div > article > div.grow > h3 > a"));
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
            // Author hay tác giả
            Author author_i = new Author();
            Post post_i = new Post();
            postList.add(post_i);
            driver.navigate().back();
        }
        driver.quit();
    }
}
