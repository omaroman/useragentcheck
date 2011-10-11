/**
 * Author: OMAROMAN
 * Date: 9/27/11
 * Time: 2:08 PM
 */

package play.modules.useragentcheck;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.mvc.Http;
import play.mvc.Router;
import play.mvc.results.Redirect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UserAgentCheckPlugin extends PlayPlugin {

    @Override
    public void beforeActionInvocation(Method actionMethod) {

        // This code is executed before controller invocation.
        // Useful for validation, where it is used by Play as well. You could also possibly put
        // additional objects into the render arguments here. Several plugins also set up some
        // variables inside thread locals to make sure they are thread safe.


        // Only check User-Agent if its configured in not 'disruptive' mode

        String block = Play.configuration.getProperty("useragentcheck.block");
        if (block != null) {
            if (!Boolean.parseBoolean(block)) {
                return;
            }
        }

        // Only check User-Agent if controller is NOT annotated with @NoUserAgentCheck

        if (actionMethod.getDeclaringClass().getAnnotation(NoUserAgentCheck.class) != null) {
            Logger.debug("@NoUserAgentCheck annotation detected");
            return;
        }
//        for (Annotation ann : actionMethod.getDeclaringClass().getAnnotations()) {
////            Logger.debug("ANNOT: %s", ann.annotationType().getName());
//            if (ann.annotationType().getName().equals(NoUserAgentCheck.class.getName())) {
//                Logger.debug("DETECTING @NoUserAgentCheck");
//                return;
//            }
//        }

        //        Logger.debug(actionMethod.toGenericString());
        if (actionMethod.toGenericString().equals("public static void controllers.useragentcheck.WebUserAgent.agent()")) {
            return;
        }

        boolean reverseProxyModuleAvailable = isReverseProxyModuleAvailable();

        Http.Request request = Http.Request.current();

        String agent = request.headers.get("user-agent").value();
        boolean acceptableAgent = UserAgentCheck.check(agent);
        Logger.debug("acceptable user-agent: %b", acceptableAgent);
        if (!acceptableAgent) {
            String url = Router.reverse("useragentcheck.WebUserAgent.block").url;
            if (reverseProxyModuleAvailable) {
                url = buildFullUrl(url);
            }
            boolean permanent = false;
            throw new Redirect(url, permanent);
        }
    }

    private boolean isReverseProxyModuleAvailable() {
        for (PlayPlugin plugin : Play.pluginCollection.getAllPlugins()) {
            if (plugin.getClass().getName().equals("play.modules.reverseproxy.ReverseProxyPlugin")) {
                if (Play.pluginCollection.isEnabled(plugin)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String buildFullUrl(String path) {

        // Let's say you want to call UrlUtility.buildFullUrl(String path);

        try {
            Class clazz = Class.forName("play.modules.reverseproxy.utilities.UrlUtility");
            Method m = clazz.getDeclaredMethod("buildFullUrl", String.class);
            m.setAccessible(true); //if security settings allow this
            return (String) m.invoke(null, path); //use null if the method is static
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
