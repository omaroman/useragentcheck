package play.modules.useragentcheck;

import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.UserAgent;
import play.Logger;
import play.Play;
import nl.bitwalker.useragentutils.Version;

public class UserAgentCheck {

	private static final String INTERNET_EXPLORER = "Internet Explorer";
	private static final String OPERA = "Opera";
	private static final String FIREFOX = "Firefox";
	private static final String CHROME = "Chrome";
	private static final String SAFARI = "Safari";

	public static boolean check(String agent) {

		Logger.debug("UserAgentCheck agent=%s",agent);

		try {

			UserAgent userAgent = UserAgent.parseUserAgentString(agent);
			Browser browser = userAgent.getBrowser().getGroup();

			// get the browser name, note that it's the group name
			String browserGroupName = browser.getName();
			
			// and then figure the major version
			Version version = browser.getVersion(agent);
			String v = version.getMajorVersion();
			int majorVersion = Integer.parseInt(v);

			// if major version is 0, agent version cannot be parsed, just ignore the test
			boolean acceptableAgent = false;

			Logger.debug("UserAgentCheck name:%s version:%s", browserGroupName, majorVersion);

			if (majorVersion > 0) {
				acceptableAgent = checkAgainst(browserGroupName, INTERNET_EXPLORER, majorVersion)
						|| checkAgainst(browserGroupName, FIREFOX, majorVersion)
						|| checkAgainst(browserGroupName, CHROME, majorVersion)
						|| checkAgainst(browserGroupName, SAFARI, majorVersion)
						|| checkAgainst(browserGroupName, OPERA, majorVersion);
			}
			Logger.debug("UserAgentCheck acceptable=%s", acceptableAgent);
			return acceptableAgent;
		} catch (Exception e) {
			Logger.warn("UserAgentCheck cannot be parserd %s", e);
			return false;
		}
	}

    public static boolean displayBanner(String agent) {
        boolean displayBannerFlag = !check(agent);  // If agent is acceptable, DO NOT show 'Banner'
        Logger.debug("UserAgentCheck displayBanner=%s", displayBannerFlag);
        return displayBannerFlag;
    }

	/**
	 * returns true if the banner needs to be displayed
     * @param browserName -
     * @param browserMatch -
     * @param majorVersion -
     * @return -
     */
	private static boolean checkAgainst(String browserName, String browserMatch, int majorVersion) {

		if (!browserMatch.equals(browserName)) {
            return false;
        }

		String capEngine = browserMatch.substring(0, 1).toUpperCase() + browserMatch.substring(1).replace(" ", "").toLowerCase();
		String minVersionStr = Play.configuration.getProperty("useragentcheck.min" + capEngine + "Version");
		int minVersion;
		if (minVersionStr == null) {
            minVersion = getDefaultMinVersion(browserMatch);
        } else {
            minVersion = Integer.parseInt(minVersionStr);
        }

//		return majorVersion < minVersion;
        return majorVersion >= minVersion;
	}

	public static int getDefaultMinVersion(String engine) {

		if (engine.equals(INTERNET_EXPLORER))
			// ie7
			return 7;
		if (engine.equals(OPERA))
			// opera 9
			return 9;
		if (engine.equals(FIREFOX))
			// firefox 5
			return 5;
		if (engine.equals(CHROME))
			// chrome 13
			return 13;
		if (engine.equals(SAFARI))
			// safari 5
			return 5;

		return 0;
	}

}
