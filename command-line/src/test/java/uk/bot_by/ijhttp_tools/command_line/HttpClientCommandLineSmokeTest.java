package uk.bot_by.ijhttp_tools.command_line;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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

}