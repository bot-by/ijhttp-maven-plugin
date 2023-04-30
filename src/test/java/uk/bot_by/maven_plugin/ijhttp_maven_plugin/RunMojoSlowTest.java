package uk.bot_by.maven_plugin.ijhttp_maven_plugin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("slow")
class RunMojoSlowTest {

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
    assertEquals("working directory is a file: " + testDirectoryLikeFile, exception.getMessage());
  }

}