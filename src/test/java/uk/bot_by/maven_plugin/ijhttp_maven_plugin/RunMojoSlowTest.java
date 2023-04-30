package uk.bot_by.maven_plugin.ijhttp_maven_plugin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("slow")
class RunMojoSlowTest {

  private static Path testDirectory;

  private RunMojo mojo;


  @BeforeAll
  static void setUpClass() {
    testDirectory = Path.of("target/test-directory");
  }

  @BeforeEach
  void setUp() throws IOException {
    Files.deleteIfExists(testDirectory);

    mojo = new RunMojo();
    mojo.setWorkingDirectory(testDirectory.toFile());
  }

  @DisplayName("Working directory: existed directory, non-existed directory")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void workingDirectory(boolean existedDirectory) throws Exception {
    // given
    if (existedDirectory) {
      Files.createDirectory(testDirectory);
    } else {
      Files.deleteIfExists(testDirectory);
    }

    // when
    var executor = mojo.getExecutor();

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
    Files.deleteIfExists(testDirectory);
    Files.createFile(testDirectory);

    // when
    Exception exception = assertThrows(MojoExecutionException.class, mojo::getExecutor);

    // then
    assertEquals("working directory is a file: target/test-directory", exception.getMessage());
  }

}