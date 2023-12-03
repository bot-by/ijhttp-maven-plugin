package uk.bot_by.ijhttp_tools.command_line.it.spring_boot_test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import org.apache.commons.exec.Executor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import uk.bot_by.ijhttp_tools.command_line.HttpClientCommandLine;

@Import(HttpClientCommandLineConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class HttpClientCommandLineApplicationTests {

  @Autowired
  private Executor executor;

  @Autowired
  private HttpClientCommandLine httpClientCommandLine;

  @Test
  void httpClientCommandLine() throws IOException {
    // given
    var files = Collections.singletonList(Path.of("echo.http").toFile());

    httpClientCommandLine.files(files);

    // when
    var exitCode = executor.execute(httpClientCommandLine.getCommandLine());

    // then
    assertEquals(0, exitCode);
  }

}
