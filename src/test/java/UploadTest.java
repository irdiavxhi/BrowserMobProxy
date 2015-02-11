import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;


public class UploadTest {
    @Test
    public void uploadTest() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://the-internet.herokuapp.com/");
        driver.findElement(By.linkText("File Upload")).click();
        driver.findElement(By.cssSelector("input[type='file']")).sendKeys("D:\\students\\Path.txt");
        driver.findElement(By.id("file-submit")).click();
        Assert.assertTrue(driver.getPageSource().contains("File uploaded"));
    }
}
