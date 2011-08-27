package useragentcheck;

import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.RenderingEngine;
import nl.bitwalker.useragentutils.UserAgent;
import nl.bitwalker.useragentutils.Version;
import play.Logger;

public class UserAgentCheck {

	public static boolean check(String agent) {

		try {
			boolean displayBannerFlag = false;

			UserAgent userAgent = UserAgent.parseUserAgentString(agent);
			Browser browser = userAgent.getBrowser();
			RenderingEngine engine = browser.getRenderingEngine();
			String renderingEngine = engine.toString();
			Version version = browser.getVersion(agent);
			String v = version.getMajorVersion();

			int majorVersion = Integer.parseInt(v);

			if (majorVersion > 0) {
				displayBannerFlag = (renderingEngine == "TRIDENT" && majorVersion <= 80);
				displayBannerFlag |= (renderingEngine == "PRESTO" && majorVersion <= 100);
				displayBannerFlag |= (renderingEngine == "GECKO" && majorVersion <= 20);
				displayBannerFlag |= (renderingEngine == "WEBKIT" && majorVersion <= 20);
			}
			return displayBannerFlag;
		} catch (Exception e) {
			Logger.warn("UserAgentCheck", agent, e);
			return false;
		}

	}
}
