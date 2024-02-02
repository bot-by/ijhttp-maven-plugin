package io.gitlab.vitalij_r2.ijhttp_tools.junit_extension.it.parameter_resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.undertow.Undertow;
import java.io.IOException;
import org.apache.commons.exec.Executor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.gitlab.vitalij_r2.ijhttp_tools.command_line.HttpClientCommandLine;
import io.gitlab.vitalij_r2.ijhttp_tools.junit_extension.HttpClientCommandLineParameters;
import io.gitlab.vitalij_r2.ijhttp_tools.junit_extension.HttpClientExecutor;

class HelloWorldHandlerTest {

  private Undertow server;

  @AfterEach
  void tearDown() {
    server.stop();
  }

  @BeforeEach
  void setUp() {
    server = Undertow.builder()                                                    //Undertow builder
        .addHttpListener(8080,
            "localhost")                                             //Listener binding
        .setHandler(new HelloWorldHandler()).build();
    server.start();
  }

  @Test
  void handleRequest(@HttpClientExecutor Executor executor,
      @HttpClientCommandLineParameters(directories = "src/test/resources/ijhttp", report = true) HttpClientCommandLine httpClientCommandLine)
      throws IOException {
    // when
    var exitCode = executor.execute(httpClientCommandLine.getCommandLine());

    // then
    assertEquals(0, exitCode);
  }

}