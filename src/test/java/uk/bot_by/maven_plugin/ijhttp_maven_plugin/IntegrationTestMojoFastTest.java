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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.bot_by.maven_plugin.ijhttp_maven_plugin.IntegrationTestMojo.LogLevel;

@ExtendWith(MockitoExtension.class)
@Tag("fast")
class IntegrationTestMojoFastTest {

  @Mock
  private Log logger;

  @DisplayName("Skip execution")
  @Test
  void skip() throws MojoExecutionException {
    // given
    var mojo = new IntegrationTestMojo(null, false, null, null, null, null, false, null, null, null,
        false, true, null);

    mojo = spy(mojo);
    when(mojo.getLog()).thenReturn(logger);

    // when
    mojo.execute();

    // then
    verify(logger).info(anyString());
  }

  @DisplayName("Files are required")
  @Test
  void filesAreRequired() {
    // given
    var mojo = new IntegrationTestMojo(null, false, null, null, null, null, false, LogLevel.BASIC,
        null, null, false, false, null);

    // when
    var exception = assertThrows(IllegalArgumentException.class, mojo::execute);

    // then
    assertEquals("files are required", exception.getMessage());
  }

  @DisplayName("Simple run without arguments")
  @Test
  void simpleRun() throws IOException {
    // given
    var file = mock(File.class);
    var mojo = new IntegrationTestMojo(null, false, null, null, null,
        Collections.singletonList(file), false, LogLevel.BASIC, null, null, false, false, null);

    when(file.getCanonicalPath()).thenReturn("*");

    // when
    var commandLine = mojo.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertAll("File argument", () -> assertThat("how much arguments", arguments, arrayWithSize(1)),
        () -> assertEquals("*", arguments[0], "filename"));
  }

  @DisplayName("Environment name")
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   ", "envname"})
  void environmentName(String name) throws IOException {
    // given
    var file = mock(File.class);
    var mojo = new IntegrationTestMojo(null, false, null, null, name,
        Collections.singletonList(file), false, LogLevel.BASIC, null, null, false, false, null);

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
  void quotedEnvironmentName() throws IOException {
    // given
    var file = mock(File.class);
    var mojo = new IntegrationTestMojo(null, false, null, null, "environment name",
        Collections.singletonList(file), false, LogLevel.BASIC, null, null, false, false, null);

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
      throws IOException {
    // given
    var file = mock(File.class);
    var mojo = new IntegrationTestMojo(null, false, null, null, null,
        Collections.singletonList(file), false, logLevel, null, null, false, false, null);

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
      String argumentValue, String argumentName) throws IOException {
    // given
    var file = mock(File.class);
    var mojo = new IntegrationTestMojo(connectTimeout, false, null, null, null,
        Collections.singletonList(file), false, LogLevel.BASIC, null, null, false, false,
        socketTimeout);

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
      String argumentName) throws IOException {
    // given
    var file = mock(File.class);
    var mojo = new IntegrationTestMojo(null, dockerMode, null, null, null,
        Collections.singletonList(file), insecure, LogLevel.BASIC, null, null, report, false, null);

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
      String argumentValue, String argumentName) throws IOException {
    // given
    var file = mock(File.class);
    var mojo = new IntegrationTestMojo(null, false, environmentFile, null, null,
        Collections.singletonList(file), false, LogLevel.BASIC, privateEnvironmentFile, null, false,
        false, null);

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
      "multi environment variables,abc=123/qwerty=xzy,N/A,--env-variables/abc=123/--env-variables/qwerty=xzy/*",
      "single private environment variable,N/A,qwerty=xzy,--private-env-variables/qwerty=xzy/*",
      "multi environment variables,N/A,abc=123/qwerty=xzy,--private-env-variables/abc=123/--private-env-variables/qwerty=xzy/*"}, nullValues = "N/A")
  void multiValueArguments(String testName,
      @ConvertWith(SlashyStringToListConverter.class) List<String> environmentVariables,
      @ConvertWith(SlashyStringToListConverter.class) List<String> privateEnvironmentVariables,
      @ConvertWith(SlashyStringToListConverter.class) List<String> expectedArguments)
      throws IOException {
    // given
    var file = mock(File.class);
    var mojo = new IntegrationTestMojo(null, false, null, environmentVariables, null,
        Collections.singletonList(file), false, LogLevel.BASIC, null, privateEnvironmentVariables,
        false, false, null);

    when(file.getCanonicalPath()).thenReturn("*");

    // when
    var commandLine = mojo.getCommandLine();

    // then
    var arguments = commandLine.getArguments();

    assertThat(testName, arguments, arrayContaining(expectedArguments.toArray()));
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