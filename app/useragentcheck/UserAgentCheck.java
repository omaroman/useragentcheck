package useragentcheck;

import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.RenderingEngine;
import nl.bitwalker.useragentutils.UserAgent;
import nl.bitwalker.useragentutils.Version;
import play.Logger;
import play.Play;

public class UserAgentCheck {

	public static boolean check(String agent) {

		Logger.debug(agent);

		try {

			UserAgent userAgent = UserAgent.parseUserAgentString(agent);
			Browser browser = userAgent.getBrowser();
			RenderingEngine engine = browser.getRenderingEngine();
			String renderingEngine = engine.toString();
			Version version = browser.getVersion(agent);
			String v = version.getMajorVersion();

			int majorVersion = Integer.parseInt(v);

			/** if 0, agent version cannot be parsed, just ignore the test **/
			boolean displayBannerFlag = false;

			if (majorVersion > 0) {
				displayBannerFlag = checkAgainst(renderingEngine, "TRIDENT",
						majorVersion)
						|| checkAgainst(renderingEngine, "GECKO", majorVersion)
						|| checkAgainst(renderingEngine, "WEBKIT", majorVersion)
						|| checkAgainst(renderingEngine, "PRESTO", majorVersion);
			}
			return displayBannerFlag;
		} catch (Exception e) {
			Logger.warn("UserAgent cannot be parserd %s", e);
			return false;
		}
	}

	/**
	 * returns true if the banner needs to be displayed
	 */
	private static boolean checkAgainst(String renderingEngine, String engine,
			int majorVersion) {

		if (!engine.equals(renderingEngine))
			return false;

		String capEngine = engine.substring(0, 1).toUpperCase()
				+ engine.substring(1).toLowerCase();
		String minVersionStr = Play.configuration
				.getProperty("useragentcheck.min" + capEngine + "Version");
		int minVersion;

		if (minVersionStr == null)
			minVersion = getDefaultMinVersion(engine);
		else
			minVersion = Integer.parseInt(minVersionStr);

		return majorVersion < minVersion;
	}

	public static int getDefaultMinVersion(String engine) {

		if (engine.equals("TRIDENT"))
			// ie7
			return 7;

		if (engine.equals("PRESTO"))
			// opera 9
			return 9;

		if (engine.equals("GECKO"))
			// firefox 5
			return 5;
		if (engine.equals("WEBKIT"))
			// webkit 2
			return 2;

		return 0;
	}

}
