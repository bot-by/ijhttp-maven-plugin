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
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
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
class CommandLineBuilderFastTest {

  @Mock
  private File file;

  CommandLineBuilder builder;

  @BeforeEach
  void setUp() throws IOException {
    builder = new CommandLineBuilder();
    builder.files(Collections.singletonList(file));
    when(file.getCanonicalPath()).thenReturn("*");
  }

  @DisplayName("Simple run without arguments")
  @Test
  void simpleRun() throws IOException {
    // when
    var commandLine = builder.build();

    // then
    var arguments = commandLine.getArguments();

    assertThat("files", arguments, arrayContaining("*"));
  }

  @DisplayName("Environment name")
  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"   ", "envname"})
  void environmentName(String name) throws IOException {
    // given
    builder.environmentName(name);

    // when
    var commandLine = builder.getCommandLine();

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
    builder.environmentName("environment name");

    // when
    var commandLine = builder.getCommandLine();

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
    builder.logLevel(logLevel);

    // when
    var commandLine = builder.getCommandLine();

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
      builder.connectTimeout(connectTimeout);
    }
    if (nonNull(socketTimeout)) {
      builder.socketTimeout(socketTimeout);
    }

    // when
    var commandLine = builder.getCommandLine();

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
    builder.dockerMode(dockerMode);
    builder.insecure(insecure);
    builder.report(report);

    // when
    var commandLine = builder.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments, arrayContaining(argumentName, "*"));
  }

  @DisplayName("File arguments")
  @ParameterizedTest
  @CsvSource(value = {"environment file,env.json,N/A,N/A,env.json,--env-file",
      "private environment file,N/A,private-env.json,N/A,private-env.json,--private-env-file",
      "report path,N/A,N/A,report-path,report-path,--report"}, nullValues = "N/A")
  void fileArguments(String testName, File environmentFile, File privateEnvironmentFile,
      File reportPath, String argumentValue, String argumentName) throws IOException {
    // given
    if (nonNull(environmentFile)) {
      builder.environmentFile(environmentFile);
    }
    if (nonNull(privateEnvironmentFile)) {
      builder.privateEnvironmentFile(privateEnvironmentFile);
    }
    if (nonNull(reportPath)) {
      builder.report(true);
      builder.reportPath(reportPath);
    }

    // when
    var commandLine = builder.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments,
        arrayContaining(equalTo(argumentName), endsWith(argumentValue), equalTo("*")));
  }

  @DisplayName("Proxy")
  @ParameterizedTest
  @CsvSource(value = {"HTTP proxy,http://localhost:3128/,--proxy|http://localhost:3128/|*",
      "SOCKS proxy,socks://localhost:9050,--proxy|socks://localhost:9050|*"})
  void singleValueArguments(String testName, String proxy,
      @ConvertWith(PipedStringToListConverter.class) List<String> expectedArguments)
      throws IOException {
    // given
    builder.proxy(proxy);

    // when
    var commandLine = builder.getCommandLine();

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
      builder.environmentVariables(environmentVariables);
    }
    if (nonNull(privateEnvironmentVariables)) {
      builder.privateEnvironmentVariables(privateEnvironmentVariables);
    }

    // when
    var commandLine = builder.getCommandLine();

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
      environmentVariables.forEach(builder::environmentVariable);
    }
    if (nonNull(privateEnvironmentVariables)) {
      privateEnvironmentVariables.forEach(builder::privateEnvironmentVariable);
    }

    // when
    var commandLine = builder.getCommandLine();

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