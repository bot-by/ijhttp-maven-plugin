package uk.bot_by.ijhttp_tools.spring_boot_test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.isA;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.List;
import org.apache.commons.exec.DefaultExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.bot_by.ijhttp_tools.command_line.LogLevel;

@Tag("fast")
class HttpClientCommandLineConfigurationTest {

  private HttpClientCommandLineConfiguration configuration;

  @BeforeEach
  void setUp() {
    configuration = new HttpClientCommandLineConfiguration();
  }

  @DisplayName("Default executor")
  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  void defaultExecutor(int timeout) {
    // when
    var executor = configuration.executor(timeout);

    // then
    assertAll("Default executor without watchdog",
        () -> assertThat("class", executor, isA(DefaultExecutor.class)),
        () -> assertNull(executor.getWatchdog(), "watchdog"));
  }

  @DisplayName("Watchdog")
  @Test
  void watchdog() {
    // when
    var executor = configuration.executor(1);

    // then
    assertAll("Default executor with watchdog",
        () -> assertThat("class", executor, isA(DefaultExecutor.class)),
        () -> assertNotNull(executor.getWatchdog(), "watchdog"));
  }

  @DisplayName("HTTP Client Command Line")
  @Test
  void httpClientCommandLine() {
    // given
    var parameters = spy(new HttpClientCommandLineParameters());

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
    verify(parameters).getFiles();
    verify(parameters).getPrivateEnvironmentFile();
    verify(parameters).getPrivateEnvironmentVariables();
    verify(parameters).getProxy();
    verify(parameters).getReportPath();
    verify(parameters).getSocketTimeout();

    assertNotNull(httpClientCommandLine);
  }

  @DisplayName("Configured HTTP Client Command Line")
  @Test
  void configuredHttpClientCommandLine() {
    // given
    var file = new File(".");
    var parameters = spy(new HttpClientCommandLineParameters());

    parameters.setDockerMode(true);
    parameters.setExecutable("test.sh");
    parameters.setInsecure(true);
    parameters.setLogLevel(LogLevel.VERBOSE);
    parameters.setReport(true);

    parameters.setConnectTimeout(1);
    parameters.setEnvironmentFile(file);
    parameters.setEnvironmentName("name");
    parameters.setEnvironmentVariables(List.of("name"));
    parameters.setFiles(List.of(file));
    parameters.setPrivateEnvironmentFile(file);
    parameters.setPrivateEnvironmentVariables(List.of("private name"));
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
    verify(parameters, times(2)).getEnvironmentFile();
    verify(parameters, times(2)).getEnvironmentName();
    verify(parameters, times(2)).getEnvironmentVariables();
    verify(parameters, times(2)).getFiles();
    verify(parameters, times(2)).getPrivateEnvironmentFile();
    verify(parameters, times(2)).getPrivateEnvironmentVariables();
    verify(parameters, times(2)).getProxy();
    verify(parameters, times(2)).getReportPath();
    verify(parameters, times(2)).getSocketTimeout();

    assertNotNull(httpClientCommandLine);
  }

}