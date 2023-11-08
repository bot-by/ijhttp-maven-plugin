package uk.bot_by.maven_plugin.ijhttp_maven_plugin;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.bot_by.maven_plugin.ijhttp_maven_plugin.RunMojo.LogLevel;

@ExtendWith(MockitoExtension.class)
@Tag("fast")
class RunMojoFastTest {

  @Captor
  private ArgumentCaptor<CommandLine> commandLineCaptor;
  @Mock
  private Executor executor;
  @Mock
  private ExecuteStreamHandler streamHandler;
  @Spy
  private RunMojo mojo;

  @BeforeEach
  void setUp() {
    mojo.setExecutable("ijhttp");
    mojo.setLog(new SystemStreamLog() {
      @Override
      public boolean isDebugEnabled() {
        return true;
      }
    });
  }

  @DisplayName("Skip execution")
  @Test
  void skip() throws MojoExecutionException, MojoFailureException {
    // given
    var logger = mock(Log.class);

    mojo.setLog(logger);
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setSkip(true);

    // when
    mojo.execute();

    // then
    verify(logger).info(anyString());
  }

  @DisplayName("Files are required")
  @Test
  void filesAreRequired() {
    // given
    mojo.setLogLevel(LogLevel.BASIC);

    // when
    var exception = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    assertEquals("files are required", exception.getMessage());
  }

  @DisplayName("Run")
  @Test
  void run() throws IOException, MojoExecutionException, MojoFailureException {
    // given
    var file = mock(File.class);

    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    when(file.getCanonicalPath()).thenReturn("*");
    when(mojo.getExecutor()).thenReturn(executor);
    when(executor.getStreamHandler()).thenReturn(streamHandler);

    // when
    mojo.execute();

    // then
    verify(executor).execute(commandLineCaptor.capture());

    var arguments = commandLineCaptor.getValue().getArguments();

    assertThat("files", arguments, arrayContaining("*"));
  }

  @DisplayName("Executor: run with an exception")
  @Test
  void runWithException() throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);

    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    when(file.getCanonicalPath()).thenReturn("*");
    when(mojo.getExecutor()).thenReturn(executor);
    when(executor.execute(isA(CommandLine.class))).thenThrow(new IOException("test exception"));
    when(executor.getStreamHandler()).thenReturn(streamHandler);

    // when
    Exception exception = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    verify(executor).execute(isA(CommandLine.class));

    assertEquals("I/O Error: test exception", exception.getMessage());
  }

  @DisplayName("Executor: run with an exception without a message")
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" "})
  void runWithExceptionWithoutMessage(String message) throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);

    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    when(file.getCanonicalPath()).thenReturn("*");
    when(mojo.getExecutor()).thenReturn(executor);
    when(executor.execute(isA(CommandLine.class))).thenThrow(new IOException(message));
    when(executor.getStreamHandler()).thenReturn(streamHandler);

    // when
    Exception exception = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    verify(executor).execute(isA(CommandLine.class));

    assertEquals("I/O Error", exception.getMessage());
  }

  @DisplayName("Executor: run with an execute exception")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void runWithExecuteException(boolean watchdogExists) throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);
    var watchdog = mock(ExecuteWatchdog.class);

    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    when(file.getCanonicalPath()).thenReturn("*");
    when(mojo.getExecutor()).thenReturn(executor);
    when(executor.execute(isA(CommandLine.class))).thenThrow(
        new ExecuteException("test execute exception", 2));
    when(executor.getStreamHandler()).thenReturn(streamHandler);
    if (watchdogExists) {
      when(executor.getWatchdog()).thenReturn(watchdog);
      when(watchdog.killedProcess()).thenReturn(false);
    }

    // when
    Exception exception = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    verify(executor).execute(isA(CommandLine.class));

    assertEquals("Execution failed: test execute exception (Exit value: 2)",
        exception.getMessage());
  }

  @DisplayName("Executor: timeout")
  @Test
  void timeoutOfExecutor() throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);
    var watchdog = mock(ExecuteWatchdog.class);

    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setTimeout(1234567);
    when(file.getCanonicalPath()).thenReturn("*");
    when(mojo.getExecutor()).thenReturn(executor);
    when(executor.execute(isA(CommandLine.class))).thenThrow(
        new ExecuteException("test execute exception", 3));
    when(executor.getStreamHandler()).thenReturn(streamHandler);
    when(executor.getWatchdog()).thenReturn(watchdog);
    when(watchdog.killedProcess()).thenReturn(true);

    // when
    Exception exception = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    verify(executor).execute(isA(CommandLine.class));

    assertEquals("Timeout. Process runs longer that 1234567 ms.", exception.getMessage());
  }

  @DisplayName("Current directory")
  @Test
  void currentDirectory() throws MojoExecutionException, IOException {
    // when
    var executor = mojo.getExecutor();

    // then
    assertEquals(".", executor.getWorkingDirectory().getName(), "working directory");
  }

  @DisplayName("Simple run without arguments")
  @Test
  void simpleRun() throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);

    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    when(file.getCanonicalPath()).thenReturn("*");

    // when
    var commandLine = mojo.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat("files", arguments, arrayContaining("*"));
  }

  @DisplayName("Environment name")
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   ", "envname"})
  void environmentName(String name) throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);

    mojo.setEnvironmentName(name);
    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    when(file.getCanonicalPath()).thenReturn("*");

    // when
    var commandLine = mojo.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    if (isNull(name) || name.isBlank()) {
      assertAll("Environment name is not set",
          () -> assertThat("how much arguments", arguments, arrayWithSize(1)),
          () -> assertEquals("*", arguments[0], "filename"));
    } else {
      assertThat("environment name", arguments, arrayContaining("--env", name, "*"));
    }
  }

  @DisplayName("Quoted environment name")
  @Test
  void quotedEnvironmentName() throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);

    mojo.setEnvironmentName("environment name");
    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    when(file.getCanonicalPath()).thenReturn("*");

    // when
    var commandLine = mojo.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat("environment name", arguments,
        arrayContaining("--env", "\"environment name\"", "*"));
  }

  @DisplayName("Logger levels")
  @ParameterizedTest
  @CsvSource(value = {"BASIC, 1, N/A", "HEADERS, 3, HEADERS",
      "VERBOSE, 3, VERBOSE"}, nullValues = "N/A")
  void loggerLevels(LogLevel logLevel, int howMuchArguments, String logLevelName)
      throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);

    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(logLevel);
    when(file.getCanonicalPath()).thenReturn("*");

    // when
    var commandLine = mojo.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat("how much arguments", arguments, arrayWithSize(howMuchArguments));
    if (nonNull(logLevelName)) {
      assertEquals(logLevelName, arguments[howMuchArguments - 2], "name of logger level");
    }
  }

  @DisplayName("Timeouts")
  @ParameterizedTest
  @CsvSource(value = {"connect timeout, 345, N/A, 345, --connect-timeout",
      "socket timeout, N/A, 765, 765, --socket-timeout"}, nullValues = "N/A")
  void timeouts(String testName, Integer connectTimeout, Integer socketTimeout,
      String argumentValue, String argumentName) throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);

    mojo.setConnectTimeout(connectTimeout);
    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setSocketTimeout(socketTimeout);
    when(file.getCanonicalPath()).thenReturn("*");

    // when
    var commandLine = mojo.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments, arrayContaining(argumentName, argumentValue, "*"));
  }

  @DisplayName("Flag arguments")
  @ParameterizedTest
  @CsvSource({"docker mode, true, false, false, --docker-mode",
      "insecure, false, true, false, --insecure", "report, false, false, true, --report"})
  void flagArguments(String testName, boolean dockerMode, boolean insecure, boolean report,
      String argumentName) throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);

    mojo.setDockerMode(dockerMode);
    mojo.setFiles(Collections.singletonList(file));
    mojo.setInsecure(insecure);
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setReport(report);
    when(file.getCanonicalPath()).thenReturn("*");

    // when
    var commandLine = mojo.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments, arrayContaining(argumentName, "*"));
  }

  @DisplayName("File arguments")
  @ParameterizedTest
  @CsvSource(value = {"environment file,env.json,N/A,env.json,--env-file",
      "private environment file,N/A,private-env.json,private-env.json,--private-env-file"}, nullValues = "N/A")
  void fileArguments(String testName, File environmentFile, File privateEnvironmentFile,
      String argumentValue, String argumentName) throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);

    mojo.setEnvironmentFile(environmentFile);
    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setPrivateEnvironmentFile(privateEnvironmentFile);
    when(file.getCanonicalPath()).thenReturn("*");

    // when
    var commandLine = mojo.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments,
        arrayContaining(equalTo(argumentName), endsWith(argumentValue), equalTo("*")));
  }

  @DisplayName("Multi-value arguments")
  @ParameterizedTest
  @CsvSource(value = {"single environment variable,abc=123,N/A,--env-variables/abc=123/*",
      "environment variable with spaces,abc=name surname,N/A,--env-variables/abc=name surname/*",
      "multi environment variables,abc=123/qwerty=xzy,N/A,--env-variables/abc=123/--env-variables/qwerty=xzy/*",
      "single private environment variable,N/A,qwerty=xzy,--private-env-variables/qwerty=xzy/*",
      "private environment variable with spaces,N/A,qwerty=xzy abc,--private-env-variables/qwerty=xzy abc/*",
      "multi environment variables,N/A,abc=123/qwerty=xzy,--private-env-variables/abc=123/--private-env-variables/qwerty=xzy/*"}, nullValues = "N/A")
  void multiValueArguments(String testName,
      @ConvertWith(SlashyStringToListConverter.class) List<String> environmentVariables,
      @ConvertWith(SlashyStringToListConverter.class) List<String> privateEnvironmentVariables,
      @ConvertWith(SlashyStringToListConverter.class) List<String> expectedArguments)
      throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);

    mojo.setEnvironmentVariables(environmentVariables);
    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setPrivateEnvironmentVariables(privateEnvironmentVariables);
    when(file.getCanonicalPath()).thenReturn("*");

    // when
    var commandLine = mojo.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments, arrayContaining(expectedArguments.toArray()));
  }

  @DisplayName("Watchdog")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void watchdog(boolean useWatchdog) throws MojoExecutionException, IOException {
    // given
    if (useWatchdog) {
      mojo.setTimeout(1000000);
    }

    // when
    var executor = mojo.getExecutor();

    // then
    if (useWatchdog) {
      assertNotNull(executor.getWatchdog(), "watchdog exists");
    } else {
      assertNull(executor.getWatchdog(), "watchdog does not exist");
    }
  }

  @DisplayName("Use Maven Logger")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void useMavenLogger(boolean quietLogs)
      throws IOException, MojoExecutionException, MojoFailureException {
    // given
    var file = mock(File.class);

    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setQuietLogs(quietLogs);
    mojo.setUseMavenLogger(true);
    when(file.getCanonicalPath()).thenReturn("*");
    when(mojo.getExecutor()).thenReturn(executor);
    when(executor.getStreamHandler()).thenReturn(streamHandler);

    // when
    mojo.execute();

    // then
    verify(executor).execute(commandLineCaptor.capture());

    var arguments = commandLineCaptor.getValue().getArguments();

    assertThat("files", arguments, arrayContaining("*"));
  }

  static class SlashyStringToListConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType)
        throws ArgumentConversionException {
      if (!targetType.isAssignableFrom(List.class)) {
        throw new ArgumentConversionException(
            "Cannot convert to " + targetType.getName() + ": " + source);
      }

      if (isNull(source)) {
        return null;
      }

      var slashyString = (String) source;

      return List.of(slashyString.split("/"));
    }

  }

}