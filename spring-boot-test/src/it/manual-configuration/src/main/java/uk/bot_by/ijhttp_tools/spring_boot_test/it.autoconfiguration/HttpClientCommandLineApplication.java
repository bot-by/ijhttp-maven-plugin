package uk.bot_by.ijhttp_tools.spring_boot_test.it.autoconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class HttpClientCommandLineApplication {

  public static void main(String[] args) {
    SpringApplication.run(HttpClientCommandLineApplication.class, args);
  }

}
