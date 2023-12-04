package uk.bot_by.ijhttp_tools.spring_boot_test.it.autoconfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import org.apache.commons.exec.Executor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import uk.bot_by.ijhttp_tools.command_line.HttpClientCommandLine;
import uk.bot_by.ijhttp_tools.command_line.LogLevel;
import uk.bot_by.ijhttp_tools.spring_boot_test.AutoConfigurationHttpClientCommandLine;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigurationHttpClientCommandLine(timeout = 7000)
class HttpClientCommandLineApplicationTests {

  @Autowired
  private Executor executor;

  @Autowired
  private HttpClientCommandLine httpClientCommandLine;

  @Value("${ijhttp.timeout}")
  private int timeout;

  @Test
  void httpClientCommandLine() throws IOException {
    // given
    var files = Collections.singletonList(Path.of("echo.http").toFile());

    httpClientCommandLine.files(files);
    httpClientCommandLine.logLevel(LogLevel.VERBOSE);
    httpClientCommandLine.environmentVariable(String.format("timeout=%s", timeout));

    // when
    var exitCode = executor.execute(httpClientCommandLine.getCommandLine());

    // then
    assertEquals(0, exitCode);
  }

}
