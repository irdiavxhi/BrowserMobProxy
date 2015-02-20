import junit.framework.Assert;
import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.http.HttpResponseInterceptor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.io.File;
import java.net.Proxy;

public class BmpTest {

    @Test(timeOut = 60000)
    public void bmpExampleTest() throws Exception {

        ProxyServer bmp = new ProxyServer(8071);

        ProxyServer proxy = new ProxyServer(8071);
        proxy.start();
        proxy.autoBasicAuthorization("", "admin", "admin");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.PROXY, proxy.seleniumProxy());

        WebDriver driver = new FirefoxDriver(caps);
        driver.get("http://the-internet.herokuapp.com/");
        driver.findElement(By.linkText("Basic Auth")).click();

        Assert.assertTrue(driver.getPageSource().contains("Congratulations! You must have the proper credentials."));
        driver.quit();

        proxy.stop();
    }

    @Test(timeOut = 60000)
    public void bmpBasicAuthPlusDownloadTest() throws Exception {

        String md = FileUtils.generateMD5(new File("D://avatar.jpg"));
        ProxyServer proxy = new ProxyServer(8071);
        proxy.start();
        proxy.autoBasicAuthorization("", "admin", "admin");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.PROXY, proxy.seleniumProxy());

        WebDriver driver = new FirefoxDriver(caps);
        driver.get("http://the-internet.herokuapp.com/");
        driver.findElement(By.linkText("Secure File Download")).click();

        Assert.assertTrue(driver.getPageSource().contains("Secure File Downloader"));

////////////////////////////////////////////////////////////////////////////////////////

        HttpResponseInterceptor downloader = new FileDownloader()
                .addContentType("image/jpeg");
        // .addContentType("application/octet-stream");.setFileName("avatar.jpg");//("image/jpeg");//("application/pdf");
        proxy.addResponseInterceptor(downloader);


        driver.findElement(By.linkText("avatar.jpg")).click();//("hello-world.pdf")).click();

        File downloadedFile = new File(driver.findElement(By.tagName("body")).getText());
        Assert.assertEquals(FileUtils.generateMD5(downloadedFile), "d56900c1544ce6a413d1e0dee32e98fc");
        driver.quit();

        proxy.stop();
    }

}
