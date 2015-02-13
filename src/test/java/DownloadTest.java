import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.http.HttpResponseInterceptor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Created by Irko on 13.02.2015.
 */
public class DownloadTest {
    @Test
    public void downloadingFiles() throws Exception {
        ProxyServer bmp = new ProxyServer(8071);
        bmp.start();

        HttpResponseInterceptor downloader = new FileDownloader()
                .addContentType("image/png");
        bmp.addResponseInterceptor(downloader);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.PROXY, bmp.seleniumProxy());

        WebDriver driver = new FirefoxDriver(caps);

        driver.get("http://the-internet.herokuapp.com");
        driver.findElement(By.linkText("File Download")).click();
        driver.findElement(By.linkText("Screen.png")).click();

        File downloadedFile = new File(driver.findElement(By.tagName("body")).getText());
        System.out.println(downloadedFile);
        System.out.println(FileUtils.generateMD5(downloadedFile));
        Assert.assertTrue(downloadedFile.exists());
        Assert.assertEquals(FileUtils.generateMD5(downloadedFile), "90843ed25773a2825754995b9233cbb8");

        Thread.sleep(30000);

        driver.quit();

        bmp.stop();
    }
}
