import junit.framework.Assert;
import net.lightbody.bmp.proxy.ProxyServer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.net.Proxy;

public class BmpTest {

    @Test(timeOut = 60000)
    public void bmpExampleTest() throws Exception {
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

}
