package io.gitlab.vitalij_r2.ijhttp_tools.spring_boot_test.it.autoconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HttpClientCommandLineApplication {

  public static void main(String[] args) {
    SpringApplication.run(HttpClientCommandLineApplication.class, args);
  }

}