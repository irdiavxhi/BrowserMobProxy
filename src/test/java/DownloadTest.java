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


public class DownloadTest {
    @Test
    public void downloadingFiles() throws Exception {
        String md = FileUtils.generateMD5(new File("D://Screen.png"));
        ProxyServer bmp = new ProxyServer(8071);

        bmp.start();
        bmp.setConnectionTimeout(100000);
        HttpResponseInterceptor downloader = new FileDownloader()
                .addContentType("image/jpeg")
                .addContentType("application/octet-stream").setFileName("avatar.jpg");//("image/jpeg");//("application/pdf");
        bmp.addResponseInterceptor(downloader);


        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.PROXY, bmp.seleniumProxy());

        WebDriver driver = new FirefoxDriver(caps);

        driver.get("http://the-internet.herokuapp.com");
        driver.findElement(By.linkText("File Download")).click();
        driver.findElement(By.linkText("avatar.jpg")).click();//("hello-world.pdf")).click();

        File downloadedFile = new File(driver.findElement(By.tagName("body")).getText());
        System.out.println(downloadedFile);
        System.out.println(FileUtils.generateMD5(downloadedFile));
        Assert.assertTrue(downloadedFile.exists());
        Assert.assertEquals(FileUtils.generateMD5(downloadedFile), "4d043e7e0e4fcc39c8af3ae931663ae0");

        Thread.sleep(30000);

        driver.quit();

        bmp.stop();
    }
}
