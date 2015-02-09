import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class AutoItTest {
    private final String login = "admin";
    private final String password = "admin";

    @Test
    public void baw() throws IOException, InterruptedException {
        WebDriver driver = new FirefoxDriver();
        ClassLoader classLoader = getClass().getClassLoader();
        File autoIt = new File(classLoader.getResource("BAW.exe").getFile());
        driver.get("http://the-internet.herokuapp.com/");
        Process p = Runtime.getRuntime().exec(autoIt.getAbsolutePath() + " " + login + " " + password);
        driver.findElement(By.linkText("Basic Auth")).click();
        Thread.sleep(3000);
        Assert.assertTrue(driver.getPageSource().contains("Congratulations! You must have the proper credentials"));
        driver.quit();
    }

}
