package core.customhandlers;

import com.google.common.annotations.Beta;

import core.Core;
import core.handlers.FileHandler;

import java.io.*;
import java.net.URL;
import java.util.Set;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Beta
public class FileDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);
    private WebDriver driver;
    private String downloadPath = System.getProperty("java.io.tmpdir");

    public FileDownloader(WebDriver driverObject) {
        this.driver = driverObject;
    }

    /**
     * Get the current location that files will be downloaded to.
     *
     * @return The filepath that the file will be downloaded to.
     */
    public String getDownloadPath() {
        return this.downloadPath;
    }

    /**
     * Set the path that files will be downloaded to.
     *
     * @param filePath The filepath that the file will be downloaded to.
     */
    public void setDownloadPath(String filePath) {
        this.downloadPath = filePath;
    }


    /**
     * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
     *
     * @param seleniumCookieSet
     * @return
     */
    private CookieStore mimicCookieState(Set<Cookie> seleniumCookieSet) {
      CookieStore tempstore = new BasicCookieStore();
        for (Cookie seleniumCookie : seleniumCookieSet)
            tempstore.addCookie((org.apache.http.cookie.Cookie) seleniumCookie);
        
        return tempstore;
    }

    /**
     * Mimic the WebDriver host configuration
     *
     * @param hostURL
     * @return
     */
    private HttpHost mimicHostConfiguration(String hostURL, int hostPort) {
    	return new HttpHost(hostURL, hostPort);   
    }

    public String fileDownloader(WebElement element) throws Exception {
        return downloader(element, "href");
    }

    public String imageDownloader(WebElement element) throws Exception {
        return downloader(element, "src");
    }

    public String downloader(WebElement element, String attribute) throws Exception {
        //Assuming that getAttribute does some magic to return a fully qualified URL
        String downloadLocation = element.getAttribute(attribute);
        if (downloadLocation.trim().equals("")) {
            throw new Exception("The element you have specified does not link to anything!");
        }
        URL downloadURL = new URL(downloadLocation);
        CloseableHttpClient client = HttpClients.createDefault();
        CookieStore cookieStore = mimicCookieState(driver.manage().getCookies());
        // Create local HTTP context
        HttpClientContext localContext = HttpClientContext.create();
        // Bind custom cookie store to the local context
        localContext.setCookieStore(cookieStore);
        HttpGet getRequest = new HttpGet(downloadURL.getPath());
        FileHandler downloadedFile = new FileHandler(downloadPath + downloadURL.getFile().replaceFirst("/|\\\\", ""), true);
        try {
            int status = client.execute(getRequest,localContext).getStatusLine().getStatusCode();
            LOGGER.info("HTTP Status {} when getting '{}'", status, downloadURL.toExternalForm());
            BufferedInputStream in = new BufferedInputStream(client.execute(getRequest,localContext).getEntity().getContent());
            int offset = 0;
            int len = 4096;
            int bytes = 0;
            byte[] block = new byte[len];
            while ((bytes = in.read(block, offset, len)) > -1) {
                downloadedFile.getWritableFileOutputStream().write(block, 0, bytes);
            }
            downloadedFile.close();
            in.close();
            LOGGER.info("File downloaded to '{}'", downloadedFile.getAbsoluteFile());
        } catch (Exception Ex) {
            LOGGER.error("Download failed: {}", Ex);
            throw new Exception("Download failed!");
        } finally {
               client.close();
        }
        return downloadedFile.getAbsoluteFile();
    }
}
