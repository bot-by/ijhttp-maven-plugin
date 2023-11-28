package uk.bot_by.maven_plugin.ijhttp_maven_plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("fast")
public class CommandLineBuilderSmokeTest {

  CommandLineBuilder builder;

  @BeforeEach
  void setUp() {
    builder = new CommandLineBuilder();
  }

  @DisplayName("Files are required")
  @Test
  void filesAreRequired() {
    // when
    var exception = assertThrows(IllegalStateException.class, builder::build);

    // then
    assertEquals("files are required", exception.getMessage());
  }

}
