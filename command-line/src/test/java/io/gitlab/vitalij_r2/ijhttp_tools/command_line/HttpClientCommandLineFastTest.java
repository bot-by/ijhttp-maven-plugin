package io.gitlab.vitalij_r2.ijhttp_tools.command_line;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static org.hamcrest.collection.ArrayMatching.arrayContainingInAnyOrder;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
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
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("fast")
class HttpClientCommandLineFastTest {

  @Mock
  private Path file;

  private HttpClientCommandLine httpClientCommandLine;

  @BeforeEach
  void setUp() throws IOException {
    httpClientCommandLine = new HttpClientCommandLine();
    httpClientCommandLine.files(file);
    when(file.toString()).thenReturn("*");
  }

  @DisplayName("Simple run without arguments")
  @Test
  void simpleRun() throws IOException {
    // when
    var commandLine = httpClientCommandLine.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat("files", arguments, arrayContaining("*"));
  }

  @DisplayName("Executable")
  @Test
  void executable() throws IOException {
    // given
    httpClientCommandLine.executable("abc");

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

    // then
    assertEquals("abc", commandLine.getExecutable());
  }

  @DisplayName("Environment name")
  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"   ", "envname"})
  void environmentName(String name) throws IOException {
    // given
    httpClientCommandLine.environmentName(name);

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    if (name.isBlank()) {
      assertAll("Environment name is not set",
          () -> assertThat("how much arguments", arguments, arrayWithSize(1)),
          () -> assertEquals("*", arguments[0], "filename"));
    } else {
      assertThat("environment name", arguments, arrayContaining("--env", name, "*"));
    }
  }

  @DisplayName("Quoted environment name")
  @Test
  void quotedEnvironmentName() throws IOException {
    // given
    httpClientCommandLine.environmentName("environment name");

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

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
      throws IOException {
    // given
    httpClientCommandLine.logLevel(logLevel);

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

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
      String argumentValue, String argumentName) throws IOException {
    // given
    if (nonNull(connectTimeout)) {
      httpClientCommandLine.connectTimeout(connectTimeout);
    }
    if (nonNull(socketTimeout)) {
      httpClientCommandLine.socketTimeout(socketTimeout);
    }

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments, arrayContaining(argumentName, argumentValue, "*"));
  }

  @DisplayName("Flag arguments")
  @ParameterizedTest
  @CsvSource({"docker mode, true, false, false, --docker-mode",
      "insecure, false, true, false, --insecure", "report, false, false, true, --report"})
  void flagArguments(String testName, boolean dockerMode, boolean insecure, boolean report,
      String argumentName) throws IOException {
    // given
    httpClientCommandLine.dockerMode(dockerMode);
    httpClientCommandLine.insecure(insecure);
    httpClientCommandLine.report(report);

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments, arrayContainingInAnyOrder(argumentName, "*"));
  }

  @DisplayName("File arguments")
  @ParameterizedTest
  @CsvSource(value = {"environment file,env.json,N/A,N/A,env.json,--env-file",
      "private environment file,N/A,private-env.json,N/A,private-env.json,--private-env-file"}, nullValues = "N/A")
  void fileArguments(String testName, Path environmentFile, Path privateEnvironmentFile,
      Path reportPath, String argumentValue, String argumentName) throws IOException {
    // given
    if (nonNull(environmentFile)) {
      httpClientCommandLine.environmentFile(environmentFile);
    }
    if (nonNull(privateEnvironmentFile)) {
      httpClientCommandLine.privateEnvironmentFile(privateEnvironmentFile);
    }
    if (nonNull(reportPath)) {
      httpClientCommandLine.report(true);
      httpClientCommandLine.reportPath(reportPath);
    }

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments,
        arrayContaining(equalTo(argumentName), endsWith(argumentValue), equalTo("*")));
  }

  @DisplayName("File arguments: report, IDEA-339395")
  @ParameterizedTest
  @CsvSource(value = {"report path,N/A,N/A,report-path,report-path,--report"}, nullValues = "N/A")
  void fileArgumentsAndReport(String testName, Path environmentFile, Path privateEnvironmentFile,
      Path reportPath, String argumentValue, String argumentName) throws IOException {
    // given
    if (nonNull(environmentFile)) {
      httpClientCommandLine.environmentFile(environmentFile);
    }
    if (nonNull(privateEnvironmentFile)) {
      httpClientCommandLine.privateEnvironmentFile(privateEnvironmentFile);
    }
    if (nonNull(reportPath)) {
      httpClientCommandLine.report(true);
      httpClientCommandLine.reportPath(reportPath);
    }

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments,
        arrayContaining(equalTo("*"), equalTo(argumentName), endsWith(argumentValue)));
  }

  @DisplayName("Proxy")
  @ParameterizedTest
  @CsvSource(value = {"HTTP proxy,http://localhost:3128/,--proxy|http://localhost:3128/|*",
      "SOCKS proxy,socks://localhost:9050,--proxy|socks://localhost:9050|*"})
  void singleValueArguments(String testName, String proxy,
      @ConvertWith(PipedStringToListConverter.class) List<String> expectedArguments)
      throws IOException {
    // given
    httpClientCommandLine.proxy(proxy);

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments, arrayContaining(expectedArguments.toArray()));
  }

  @DisplayName("Multi-value arguments")
  @ParameterizedTest
  @CsvSource(value = {"single environment variable,abc=123,N/A,--env-variables|abc=123|*",
      "environment variable with spaces,abc=name surname,N/A,--env-variables|abc=name surname|*",
      "multi environment variables,abc=123|qwerty=xzy,N/A,--env-variables|abc=123|--env-variables|qwerty=xzy|*",
      "single private environment variable,N/A,qwerty=xzy,--private-env-variables|qwerty=xzy|*",
      "private environment variable with spaces,N/A,qwerty=xzy abc,--private-env-variables|qwerty=xzy abc|*",
      "multi environment variables,N/A,abc=123|qwerty=xzy,--private-env-variables|abc=123|--private-env-variables|qwerty=xzy|*"}, nullValues = "N/A")
  void multiValueArguments(String testName,
      @ConvertWith(PipedStringToListConverter.class) List<String> environmentVariables,
      @ConvertWith(PipedStringToListConverter.class) List<String> privateEnvironmentVariables,
      @ConvertWith(PipedStringToListConverter.class) List<String> expectedArguments)
      throws IOException {
    // given
    if (nonNull(environmentVariables)) {
      httpClientCommandLine.environmentVariables(environmentVariables);
    }
    if (nonNull(privateEnvironmentVariables)) {
      httpClientCommandLine.privateEnvironmentVariables(privateEnvironmentVariables);
    }

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments, arrayContaining(expectedArguments.toArray()));
  }

  @DisplayName("Multi-value arguments as single ones")
  @ParameterizedTest
  @CsvSource(value = {"single environment variable,abc=123,N/A,--env-variables|abc=123|*",
      "environment variable with spaces,abc=name surname,N/A,--env-variables|abc=name surname|*",
      "multi environment variables,abc=123|qwerty=xzy,N/A,--env-variables|abc=123|--env-variables|qwerty=xzy|*",
      "single private environment variable,N/A,qwerty=xzy,--private-env-variables|qwerty=xzy|*",
      "private environment variable with spaces,N/A,qwerty=xzy abc,--private-env-variables|qwerty=xzy abc|*",
      "multi environment variables,N/A,abc=123|qwerty=xzy,--private-env-variables|abc=123|--private-env-variables|qwerty=xzy|*"}, nullValues = "N/A")
  void multiValueArgumentsAsSingleOnes(String testName,
      @ConvertWith(PipedStringToListConverter.class) List<String> environmentVariables,
      @ConvertWith(PipedStringToListConverter.class) List<String> privateEnvironmentVariables,
      @ConvertWith(PipedStringToListConverter.class) List<String> expectedArguments)
      throws IOException {
    // given
    if (nonNull(environmentVariables)) {
      environmentVariables.forEach(httpClientCommandLine::environmentVariable);
    }
    if (nonNull(privateEnvironmentVariables)) {
      privateEnvironmentVariables.forEach(httpClientCommandLine::privateEnvironmentVariable);
    }

    // when
    var commandLine = httpClientCommandLine.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments, arrayContaining(expectedArguments.toArray()));
  }

  static class PipedStringToListConverter extends SimpleArgumentConverter {

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

      return List.of(slashyString.split("\\|"));
    }

  }

}