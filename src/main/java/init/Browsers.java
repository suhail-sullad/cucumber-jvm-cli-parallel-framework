package init;

public enum Browsers {

	FIREFOX("firefox"), CHROME("chrome"), INTERNET_EXPLORER("ie");
	String browser_short_name;

	Browsers(String bsName) {
		this.browser_short_name = bsName;
	}
}
