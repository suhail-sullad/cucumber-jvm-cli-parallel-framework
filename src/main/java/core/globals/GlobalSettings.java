

package core.globals;

import java.text.SimpleDateFormat;
import java.util.*;

import core.BrowserConfiguration.htmlUnitEmulation;
import core.BrowserConfiguration.selectedBrowser;
import core.Configuration;
import org.openqa.selenium.WebElement;

public class GlobalSettings implements Configuration {

    //Read in in settings from properties files
    ResourceBundle _prop = ResourceBundle.getBundle("ebselen");
    //Settings

    private static int defaultTimeout = 10000;
    private static int pollInterval = 100;
    private static int smallThinkTime = 3000;
    private static int bigThinkTime = 10000;
    private static boolean loggedIn = false;
    private static boolean started = false;
    private static boolean useSeleniumBackedWebDriver = false;
    private static String releaseNumber = ""; //Optional field to track release being tested.
    //Set by constructor
    private static boolean debugSetting;
    private static boolean verboseDebugSetting;
    private static osList osSetting;
    private static selectedBrowser browserSetting;
    private static String envHash;
    private static String dateTimeToday;
    private static String dateToday;
    private static boolean useDBBackend;
    private static String siteSubDomain;
    private static String siteDomain;
    private static String path;
    private static String dbConnection;
    private static String chromeDriverLocation;
    public static final String sepReg = "(\\\\|/)";
    private static EnumMap<selectSite, String> homePages = new EnumMap<selectSite, String>(selectSite.class);
    private static selectSite currentlySelectedSite;
    private static boolean checkHomepageLoaded;
    private static WebElement homepageElement;
    private static htmlUnitEmulation htmlunitEmulation;

    public GlobalSettings() {
        osSetting(getString("os"));
        if (!browserSetting(getString("browser"))) {
            System.err.println("Invalid Browser Setting Detected!");
        }
        setEnvHash();
        setDateToday();
        setDateTimeToday();
        setHomePages();

        useDBBackend(Boolean.parseBoolean(getString("enableDBConnection")));
        if (useDBBackend()) {
            databaseConnection(getString("dbTestData"));
        }
        setHTMLUnitEmulation(getString("htmlunitemulation"));
    }

    public String getString(String Key) {
        String res = null;
        try {
            res = _prop.getString(Key);
        } catch (Exception Ex) {
            System.err.println("[ERROR] Expected Key " + Key + " does not exist in the properties file, returning null!");
        }
        return res;
    }

    public int getInt(String Key) {
        try {
            return Integer.parseInt(_prop.getString(Key));
        } catch (Exception x) {
            System.err.println("[ERROR] Expected Key " + Key + " does not exist in the properties file, returning 0!");
        }
        return 0;
    }

    public Boolean getBoolean(String Key) throws Exception {
        try {
            return Boolean.parseBoolean(_prop.getString(Key));
        } catch (Exception x) {
            System.err.println("[ERROR] Expected Key " + Key + " does not exist in the properties file, returning null!");
        }
        return null;
    }

    public String path() {
        return path;
    }

    public void path(String value) {
        path = value;
    }

    public int defaultTimeout() {
        return defaultTimeout;
    }

    public void defaultTimeout(int value) {
        defaultTimeout = value;
    }

    public void pollInterval(int value) {
        pollInterval = value;
    }

    public int pollInterval() {
        return pollInterval;
    }

    public String siteSubDomain() {
        return siteSubDomain;
    }

    public String siteDomain() {
        return siteDomain;
    }

    public boolean loggedIn() {
        return loggedIn;
    }

    public void loggedIn(boolean value) {
        loggedIn = value;
    }

    public boolean useDBBackend() {
        return useDBBackend;
    }

    public void useDBBackend(boolean value) {
        useDBBackend = value;
    }

    public boolean osSetting(String value) {
        for (osList os : osList.values()) {
            if (os.toString().toLowerCase().equals(value)) {
                osSetting = os;
                return true;
            }
        }
        return false;
    }

    private osList osSetting() {
        return osSetting;
    }

    private boolean browserSetting(String value) {
        for (selectedBrowser browser : selectedBrowser.values()) {
            if (browser.toString().toLowerCase().equals(value)) {
                browserSetting = browser;
                return true;
            }
        }
        return false;
    }

    public selectedBrowser browserSetting() {
        return browserSetting;
    }

    public void setEnvHash() {
        envHash = (osSetting().toString().substring(0, 3) + browserSetting().toString().substring(0, 1));
    }

    public String getEnvHash() {
        return envHash;
    }

    public boolean started() {
        return started;
    }

    public void started(boolean value) {
        started = value;
    }

    public void databaseConnection(String value) {
        dbConnection = value;
    }

    public String databaseConnection() {
        return dbConnection;
    }

    public void smallThinkTime(int value) {
        smallThinkTime = value;
    }

    public int smallThinkTime() {
        return smallThinkTime;
    }

    public void bigThinkTime(int value) {
        bigThinkTime = value;
    }

    public int bigThinkTime() {
        return bigThinkTime;
    }

    private void setDateToday() {
        Calendar now = Calendar.getInstance();
        dateToday = now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.YEAR);
    }

    public String dateToday() {
        return dateToday;
    }

    private void setDateTimeToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar now = Calendar.getInstance();
        dateTimeToday = sdf.format(now.getTime());
    }

    public String dateTimeToday() {
        return dateTimeToday;
    }

    public void useSelenium(boolean value) {
        useSeleniumBackedWebDriver = value;
    }

    public boolean useSelenium() {
        return useSeleniumBackedWebDriver;
    }

    public void setReleaseNumber(String value) {
        releaseNumber = value;
    }

    public String releaseNumber() {
        return releaseNumber;
    }

    public void chromeDriverLocation(String value) {
        chromeDriverLocation = value;
    }

    public String chromeDriverLocation() {
        return chromeDriverLocation;
    }

    public final String setFilePath(String value) {
        String[] pathExploded = value.split(sepReg);
        String tempPath = "";
        for (String pathSegment : pathExploded) {
            tempPath += pathSegment + System.getProperty("file.separator");
        }
        return tempPath;
    }

    private void setHomePages() {
        for (selectSite page : selectSite.values()) {
            homePages.put(page, getString(page.toString().toLowerCase()).trim());
        }
    }

    public String getHomePage(selectSite page) {
        return homePages.get(page);
    }

    /**
     * Takes the site url set in the properties file and splits it out into domain and subdomain
     * {This assume that all domains are two part i.e. foo.com.  This will not work for a hostname i.e. localhost)
     *
     * @param site
     */
    public void setCurrentlySelectedSite(selectSite site) {
        currentlySelectedSite = site;
        String[] urlComponents = homePages.get(currentlySelectedSite).split("\\.");
        siteDomain = urlComponents[urlComponents.length - 2] + "." + urlComponents[urlComponents.length - 1];
        siteSubDomain = urlComponents[0];
        for (int i = 1; i < urlComponents.length - 2; i++) {
            siteSubDomain += "." + urlComponents[i];
        }
    }

    public selectSite getCurrentlySelectedSite() {
        return currentlySelectedSite;
    }

    private void setCheckHomepageLoaded(boolean value) {
        checkHomepageLoaded = value;
    }

    public boolean checkHomepageLoaded() {
        return checkHomepageLoaded;
    }

    public void setHomepageElement(WebElement element) {
        homepageElement = element;
        setCheckHomepageLoaded(true);
    }

    public void clearHomepageElement() {
        homepageElement = null;
        setCheckHomepageLoaded(false);
    }

    public WebElement getHomepageElement() {
        return homepageElement;
    }

    public void setHTMLUnitEmulation(String browserType) {
        if (browserType.equals("none")) {
            htmlunitEmulation = htmlUnitEmulation.NONE;
        } else if (browserType.equals("firefox")) {
            htmlunitEmulation = htmlUnitEmulation.FIREFOX;
        } else if (browserType.equals("ie6")) {
            htmlunitEmulation = htmlUnitEmulation.IE6;
        } else if (browserType.equals("ie7")) {
            htmlunitEmulation = htmlUnitEmulation.IE7;
        } else if (browserType.equals("ie8")) {
            htmlunitEmulation = htmlUnitEmulation.IE8;
        }
    }

    public htmlUnitEmulation getHTMLUnitEmulation() {
        return htmlunitEmulation;
    }
}