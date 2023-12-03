package uk.bot_by.ijhttp_tools.command_line.it.spring_boot_test;

import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import uk.bot_by.ijhttp_tools.command_line.HttpClientCommandLine;

@Configuration
public class HttpClientCommandLineConfiguration {

  @Bean
  static Executor executor() {
    return new DefaultExecutor();
  }

  @Bean
  static HttpClientCommandLine httpClientCommandLine() {
    return new HttpClientCommandLine();
  }

}
