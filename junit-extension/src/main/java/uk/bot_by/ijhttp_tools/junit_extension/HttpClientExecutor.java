package uk.bot_by.ijhttp_tools.junit_extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HTTP Client Executor.
 *
 * @author Vitalij Berdinskih
 * @since 1.3.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface HttpClientExecutor {

  /**
   * The timeout for the process in milliseconds. It must be greater than 0.
   *
   * @return process timeout
   */
  int timeout() default -1;

}
