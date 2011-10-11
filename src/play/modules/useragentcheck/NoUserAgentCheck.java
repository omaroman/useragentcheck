/**
 * Author: OMAROMAN
 * Date: 9/28/11
 * Time: 10:16 AM
 */

package play.modules.useragentcheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoUserAgentCheck {

}
