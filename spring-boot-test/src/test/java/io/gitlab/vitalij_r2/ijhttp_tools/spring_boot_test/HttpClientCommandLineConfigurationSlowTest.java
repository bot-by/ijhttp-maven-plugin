package io.gitlab.vitalij_r2.ijhttp_tools.spring_boot_test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.gitlab.vitalij_r2.ijhttp_tools.command_line.LogLevel;

@Tag("slow")
class HttpClientCommandLineConfigurationSlowTest {

  private HttpClientCommandLineConfiguration configuration;
  private Logger logger;

  @BeforeEach
  void setUp() {
    configuration = new HttpClientCommandLineConfiguration();
    logger = LoggerFactory.getLogger(configuration.getClass());
  }

  @AfterEach
  void tearDown() {
    clearInvocations(logger);
  }

  @DisplayName("Default executor")
  @Test
  void defaultExecutor() {
    // when
    configuration.executor(-1);

    // then
    verifyNoInteractions(logger);
  }

  @DisplayName("Logged timeout")
  @Test
  void loggerTimeout() {
    // given
    when(logger.isDebugEnabled()).thenReturn(true);

    // when
    configuration.executor(1);

    // then
    verify(logger).isDebugEnabled();
    verify(logger).debug(anyString());
  }

  @DisplayName("HTTP Client Command Line")
  @Test
  void httpClientCommandLine() {
    // given
    var file = Paths.get("test-file");
    var parameters = spy(new HttpClientCommandLineParameters());

    parameters.setFiles(List.of(file));

    // when
    var httpClientCommandLine = configuration.httpClientCommandLine(parameters);

    // then
    verify(parameters).isDockerMode();
    verify(parameters).getExecutable();
    verify(parameters).isInsecure();
    verify(parameters).getLogLevel();
    verify(parameters).isReport();

    verify(parameters).getConnectTimeout();
    verify(parameters).getEnvironmentFile();
    verify(parameters).getEnvironmentName();
    verify(parameters).getEnvironmentVariables();
    verify(parameters, times(2)).getFiles();
    verify(parameters).getPrivateEnvironmentFile();
    verify(parameters).getPrivateEnvironmentVariables();
    verify(parameters).getProxy();
    verify(parameters).getReportPath();
    verify(parameters).getSocketTimeout();

    assertAll("Default HTTP Client Command Client", () -> assertNotNull(httpClientCommandLine),
        () -> assertEquals("ijhttp", httpClientCommandLine.getCommandLine().getExecutable()),
        () -> assertThat(httpClientCommandLine.getCommandLine().getArguments(),
            arrayContaining(endsWith("test-file"))));
  }

  @DisplayName("Configured HTTP Client Command Line")
  @Test
  void configuredHttpClientCommandLine() {
    // given
    var file = Paths.get("test-file");
    var parameters = spy(new HttpClientCommandLineParameters());

    parameters.setDirectories(List.of(Path.of("src/test")));
    parameters.setDockerMode(true);
    parameters.setExecutable("test.sh");
    parameters.setInsecure(true);
    parameters.setLogLevel(LogLevel.VERBOSE);
    parameters.setReport(true);

    parameters.setConnectTimeout(1);
    parameters.setEnvironmentFile(file);
    parameters.setEnvironmentName("name");
    parameters.setEnvironmentVariables(List.of("public"));
    parameters.setFiles(List.of(file));
    parameters.setPrivateEnvironmentFile(file);
    parameters.setPrivateEnvironmentVariables(List.of("private"));
    parameters.setProxy("proxy");
    parameters.setReportPath(file);
    parameters.setSocketTimeout(2);

    // when
    var httpClientCommandLine = configuration.httpClientCommandLine(parameters);

    // then
    verify(parameters).isDockerMode();
    verify(parameters).getExecutable();
    verify(parameters).isInsecure();
    verify(parameters).getLogLevel();
    verify(parameters).isReport();

    verify(parameters, times(2)).getConnectTimeout();
    verify(parameters, times(2)).getDirectories();
    verify(parameters, times(2)).getEnvironmentFile();
    verify(parameters, times(2)).getEnvironmentName();
    verify(parameters, times(2)).getEnvironmentVariables();
    verify(parameters, times(2)).getFiles();
    verify(parameters, times(2)).getPrivateEnvironmentFile();
    verify(parameters, times(2)).getPrivateEnvironmentVariables();
    verify(parameters, times(2)).getProxy();
    verify(parameters, times(2)).getReportPath();
    verify(parameters, times(2)).getSocketTimeout();

    assertAll("Configured HTTP Client Command Client", () -> assertNotNull(httpClientCommandLine),
        () -> assertEquals("test.sh", httpClientCommandLine.getCommandLine().getExecutable()),
        () -> assertThat(httpClientCommandLine.getCommandLine().getArguments(),
            arrayContaining(equalTo("--docker-mode"), equalTo("--insecure"), equalTo("--log-level"),
                equalTo("VERBOSE"), equalTo("--connect-timeout"), equalTo("1"),
                equalTo("--socket-timeout"), equalTo("2"), equalTo("--env"), equalTo("name"),
                equalTo("--env-file"), endsWith("test-file"), equalTo("--env-variables"),
                equalTo("public"), equalTo("--private-env-file"), endsWith("test-file"),
                equalTo("--private-env-variables"), equalTo("private"), equalTo("--proxy"),
                equalTo("proxy"), endsWith("test-file"), endsWith("directory-test.http"),
                equalTo("--report"), endsWith("test-file"))));
  }

}