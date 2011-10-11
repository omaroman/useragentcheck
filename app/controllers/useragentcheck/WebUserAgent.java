/**
 * Author: OMAROMAN
 * Date: 9/27/11
 * Time: 5:48 PM
 */

package controllers.useragentcheck;

import play.modules.useragentcheck.NoUserAgentCheck;
import play.mvc.Controller;

@NoUserAgentCheck
public class WebUserAgent extends Controller {

    public static void block() {
        render();
    }
}
