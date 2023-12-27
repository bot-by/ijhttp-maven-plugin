package uk.bot_by.ijhttp_tools.command_line;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static org.hamcrest.collection.ArrayMatching.arrayContainingInAnyOrder;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("fast")
public class HttpClientCommandLineSmokeTest {

  private HttpClientCommandLine builder;

  @BeforeEach
  void setUp() {
    builder = new HttpClientCommandLine();
  }

  @DisplayName("Files are required")
  @Test
  void filesAreRequired() {
    // when
    var exception = assertThrows(IllegalStateException.class, builder::getCommandLine);

    // then
    assertEquals("files are required", exception.getMessage());
  }

  @DisplayName("Folder")
  @ParameterizedTest(name = "{0}")
  @CsvSource({"Folder,src/test/resources/folder,google.http",
      "Subdirectory,src/test/resources/directory,bing.http"})
  void folder(String name, String path, String filename) throws IOException {
    // given
    var folder = Paths.get(path);
    builder.directories(folder);

    // when
    var commandLine = assertDoesNotThrow(builder::getCommandLine);

    // then
    assertThat(commandLine.getArguments(), arrayContaining(endsWith(filename)));
  }

  @DisplayName("Directories")
  @Test
  void subdirectory() throws IOException {
    // given
    var folder = Paths.get("src/test/resources/folder");
    var directoryWithSubdirectory = Paths.get("src/test/resources/directory");

    builder.directories(folder, directoryWithSubdirectory);

    // when
    var commandLine = assertDoesNotThrow(builder::getCommandLine);

    // then
    assertThat(commandLine.getArguments(),
        arrayContainingInAnyOrder(endsWith("bing.http"), endsWith("google.http")));
  }

  @DisplayName("Max-depth")
  @Test
  void maxDepth() throws IOException {
    // given
    var folder = Paths.get("src/test/resources/folder");
    var directoryWithSubdirectory = Paths.get("src/test/resources/directory");

    builder.directories(folder, directoryWithSubdirectory);
    builder.maxDepth(1);

    // when
    var commandLine = assertDoesNotThrow(builder::getCommandLine);

    // then
    assertThat(commandLine.getArguments(), arrayContaining(endsWith("google.http")));
  }

}