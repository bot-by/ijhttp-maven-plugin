package io.gitlab.vitalij_r2.ijhttp_tools.maven_plugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import io.gitlab.vitalij_r2.ijhttp_tools.command_line.LogLevel;

@ExtendWith(MockitoExtension.class)
@Tag("slow")
class RunMojoSlowTest {

  @Captor
  private ArgumentCaptor<CommandLine> commandLineCaptor;
  @Mock
  private Executor executor;
  @Mock
  private ExecuteStreamHandler streamHandler;
  @Spy
  private RunMojo mojo;

  @BeforeEach
  void setUp() throws IOException {
    mojo.setExecutable("ijhttp");
    mojo.setLog(new SystemStreamLog() {
      @Override
      public boolean isDebugEnabled() {
        return true;
      }
    });
  }

  @DisplayName("Directories")
  @Test
  void directories() throws IOException, MojoExecutionException, MojoFailureException {
    // given
    var directory = new File("src/test/resources/directory");

    mojo.setDirectories(List.of(directory));
    mojo.setLogLevel(LogLevel.BASIC);
    when(mojo.getExecutor()).thenReturn(executor);
    when(executor.getStreamHandler()).thenReturn(streamHandler);

    // when
    mojo.execute();

    // then
    verify(executor).execute(commandLineCaptor.capture());

    var arguments = commandLineCaptor.getValue().getArguments();

    assertThat("directories", arguments, arrayContaining(endsWith("bing.http")));
  }

  @DisplayName("Working directory: existed directory, non-existed directory")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void workingDirectory(boolean existedDirectory) throws Exception {
    // given
    var testDirectory = Files.createTempDirectory("workdir-");

    if (!existedDirectory) {
      Files.deleteIfExists(testDirectory);
    }
    mojo.setWorkingDirectory(testDirectory.toFile());

    // when
    var executor = assertDoesNotThrow(mojo::getExecutor);

    // then
    assertAll("working directory",
        () -> assertEquals(testDirectory.toFile(), executor.getWorkingDirectory(),
            "working directory"),
        () -> assertTrue(Files.exists(testDirectory), "test directory exists"));
  }

  @DisplayName("Working directory is a file")
  @Test
  void workingDirectoryIsFile() throws IOException, MojoExecutionException {
    // given
    var testDirectoryLikeFile = Files.createTempFile("workdir-", "-test");

    mojo.setWorkingDirectory(testDirectoryLikeFile.toFile());

    // when
    Exception exception = assertThrows(MojoExecutionException.class, mojo::getExecutor);

    // then
    assertEquals("the working directory is a file: " + testDirectoryLikeFile,
        exception.getMessage());
  }

  @DisplayName("Output file")
  @Test
  void outputFile() throws IOException, MojoExecutionException, MojoFailureException {
    // given
    var outputFile = Files.createTempFile("http-client-", ".log");
    var file = mock(File.class);
    var path = mock(Path.class);

    mojo.setFiles(List.of(file));
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setOutputFile(outputFile.toFile());
    when(file.toPath()).thenReturn(path);
    when(path.toString()).thenReturn("*");
    when(mojo.getExecutor()).thenReturn(executor);
    when(executor.getStreamHandler()).thenReturn(streamHandler);

    // when
    mojo.execute();

    // then
    assertTrue(Files.exists(outputFile));
  }

  @DisplayName("Parent directories do not exist")
  @Test
  void parentDirectories() throws IOException, MojoExecutionException {
    // given
    var parentDirectories = Path.of(Files.createTempFile("parent-", "-directory").toString(),
        "second");
    var outputFile = Path.of(parentDirectories.toString(), "http-client.log");
    var file = mock(File.class);
    var path = mock(Path.class);

    mojo.setFiles(List.of(file));
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setOutputFile(outputFile.toFile());
    when(file.toPath()).thenReturn(path);
    when(path.toString()).thenReturn("*");
    when(mojo.getExecutor()).thenReturn(executor);

    // when
    Exception exception = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    assertThat(exception.getMessage(), startsWith("I/O Error: "));
  }

  @DisplayName("Use Maven Logger")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void useMavenLogger(boolean quietLogs) throws IOException {
    // given
    var file = mock(File.class);
    var files = new ArrayList<File>();
    var path = mock(Path.class);

    files.add(file);

    mojo.setExecutable("./test-exit-code.sh");
    mojo.setFiles(files);
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setQuietLogs(quietLogs);
    mojo.setUseMavenLogger(true);
    when(file.toPath()).thenReturn(path);
    when(path.toString()).thenReturn("0");

    // when
    assertDoesNotThrow(mojo::execute);
  }

  @DisplayName("stderr")
  @ParameterizedTest
  @ValueSource(ints = {1, 2})
  void stderr(int exitCode) throws IOException, MojoExecutionException {
    // given
    var file = mock(File.class);
    var files = new ArrayList<File>();
    var path = mock(Path.class);

    files.add(file);

    mojo.setExecutable("./test-exit-code.sh");
    mojo.setFiles(files);
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setUseMavenLogger(true);
    when(file.toPath()).thenReturn(path);
    when(path.toString()).thenReturn(Integer.toString(exitCode));
    when(mojo.getExecutor()).thenAnswer(invocationOnMock -> {
      var executor = DefaultExecutor.builder().get();

      executor.setExitValue(exitCode);

      return executor;
    });

    // when
    assertDoesNotThrow(mojo::execute);
  }

}