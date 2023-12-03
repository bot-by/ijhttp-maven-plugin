package uk.bot_by.ijhttp_tools.command_line.it.spring_boot_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class HttpClientCommandLineApplication {

  public static void main(String[] args) {
    SpringApplication.run(HttpClientCommandLineApplication.class, args);
  }

}
