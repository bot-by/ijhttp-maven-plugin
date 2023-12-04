package uk.bot_by.ijhttp_tools.spring_boot_test;

import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import uk.bot_by.ijhttp_tools.command_line.HttpClientCommandLine;

@ConditionalOnWebApplication(
    type = Type.SERVLET
)
public class HttpClientCommandLineConfiguration {

  private final Logger logger = LoggerFactory.getLogger(HttpClientCommandLineConfiguration.class);

  @Bean
  @ConditionalOnMissingBean
  Executor executor(@Value("${ijhttp.timeout:0}") int timeout) {
    var executor = new DefaultExecutor();

    if (timeout > 0) {
      executor.setWatchdog(new ExecuteWatchdog(timeout));
      if (logger.isDebugEnabled()) {
        logger.debug(String.format("Set the watchdog (%s) ms", timeout));
      }
    }

    return executor;
  }

  @Bean
  HttpClientCommandLine httpClientCommandLine() {
    return new HttpClientCommandLine();
  }

}
