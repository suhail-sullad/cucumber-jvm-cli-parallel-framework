package initilization;

import java.util.Hashtable;

public class ScenarioContext {
	private static Hashtable<String, String> scenariodata = new Hashtable<String, String>();
	private static String scenarioname;

	public static String getvalue(String scenarioname, String variable) {
		return scenariodata.get(scenarioname + "." + variable);
	}

	public static void addvalue(String scenarioname, String variable,
			String value) {
		scenariodata.put(scenarioname + "." + variable, value);
	}

	public static String getScenarioname() {
		return scenarioname;
	}

	public static void setScenarioname(String scenarioname) {
		ScenarioContext.scenarioname = scenarioname;
	}

	public static void flushscenariodata() {
		ScenarioContext.scenariodata.clear();
	}

}
