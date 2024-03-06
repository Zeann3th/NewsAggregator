package webcrawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.concurrent.TimeUnit;

public class NYTimesCrawler extends Crawler{
    public void crawl() {
        WebDriver driver = new EdgeDriver();
        driver.get("https://www.nytimes.com/");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"app\"]/div[2]/div[2]/header/section[1]/div[1]/div[2]/button"));
        searchButton.click();
        WebElement searchBar = driver.findElement(By.xpath("//*[@id=\"search-input\"]/form/div/input"));
        searchBar.clear();
        searchBar.sendKeys("Blockchain");
        WebElement searchGo = driver.findElement(By.xpath("//*[@id=\"search-input\"]/form/button"));
        searchGo.click();
        driver.quit();
    }
}
