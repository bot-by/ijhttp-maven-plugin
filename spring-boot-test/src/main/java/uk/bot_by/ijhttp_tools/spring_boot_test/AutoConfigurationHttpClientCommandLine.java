package uk.bot_by.ijhttp_tools.spring_boot_test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.properties.PropertyMapping;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration
@PropertyMapping("ijhttp")
public @interface AutoConfigurationHttpClientCommandLine {

  /**
   * The timeout for the process in milliseconds.
   *
   * @return process timeout
   */
  @PropertyMapping("timeout") int timeout() default -1;

}
