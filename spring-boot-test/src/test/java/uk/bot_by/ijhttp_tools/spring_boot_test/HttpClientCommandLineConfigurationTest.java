package uk.bot_by.ijhttp_tools.spring_boot_test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.isA;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.exec.DefaultExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("fast")
class HttpClientCommandLineConfigurationTest {

  private HttpClientCommandLineConfiguration configuration;

  @BeforeEach
  void setUp() {
    configuration = new HttpClientCommandLineConfiguration();
  }

  @DisplayName("Default executor")
  @Test
  void defaultExecutor() {
    // when
    var executor = configuration.executor(0);

    // then
    assertAll("Default executor without watchdog",
        () -> assertThat(executor, isA(DefaultExecutor.class)),
        () -> assertNull(executor.getWatchdog()));
  }

  @DisplayName("Watchdog")
  @Test
  void watchdog() {
    // when
    var executor = configuration.executor(1);

    // then
    assertAll("Default executor without watchdog",
        () -> assertThat(executor, isA(DefaultExecutor.class)),
        () -> assertNotNull(executor.getWatchdog()));
  }

  @DisplayName("HTTP Client Command Line")
  @Test
  void httpClientCommandLine() {
    // when and then
    assertNotNull(configuration.httpClientCommandLine());
  }

}