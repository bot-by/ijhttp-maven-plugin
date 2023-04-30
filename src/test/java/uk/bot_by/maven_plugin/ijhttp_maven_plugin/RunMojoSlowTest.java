package uk.bot_by.maven_plugin.ijhttp_maven_plugin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.bot_by.maven_plugin.ijhttp_maven_plugin.RunMojo.LogLevel;

@ExtendWith(MockitoExtension.class)
@Tag("slow")
class RunMojoSlowTest {

  @Mock
  private Executor executor;
  @Mock
  private ExecuteStreamHandler streamHandler;

  private RunMojo mojo;

  @BeforeEach
  void setUp() throws IOException {
    mojo = new RunMojo();
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
  void outputFile() throws IOException, MojoExecutionException {
    // given
    var outputFile = Files.createTempFile("http-client-", ".log");
    var file = mock(File.class);
    var mojo = spy(this.mojo);

    mojo.setExecutable("ijhttp");
    mojo.setFiles(Collections.singletonList(file));
    mojo.setLogLevel(LogLevel.BASIC);
    mojo.setOutputFile(outputFile.toFile());
    when(file.getCanonicalPath()).thenReturn("*");
    when(mojo.getExecutor()).thenReturn(executor);
    when(executor.getStreamHandler()).thenReturn(streamHandler);

    // when
    mojo.execute();

    // then
    assertTrue(Files.exists(outputFile));
  }

}