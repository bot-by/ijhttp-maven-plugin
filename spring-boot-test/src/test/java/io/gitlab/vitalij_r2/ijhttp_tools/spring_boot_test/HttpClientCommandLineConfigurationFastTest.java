package io.gitlab.vitalij_r2.ijhttp_tools.spring_boot_test;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("fast")
class HttpClientCommandLineConfigurationFastTest {

  private HttpClientCommandLineConfiguration configuration;

  @BeforeEach
  void setUp() {
    configuration = new HttpClientCommandLineConfiguration();
  }

  @DisplayName("Default executor")
  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  void defaultExecutor(int timeout) {
    // when
    var executor = configuration.executor(timeout);

    // then
    assertAll("Default executor without watchdog",
        () -> assertThat("class", executor, isA(DefaultExecutor.class)),
        () -> assertNull(executor.getWatchdog(), "watchdog"));
  }

  @DisplayName("Watchdog")
  @Test
  void watchdog() {
    // when
    var executor = configuration.executor(1);

    // then
    assertAll("Default executor with watchdog",
        () -> assertThat("class", executor, isA(DefaultExecutor.class)),
        () -> assertNotNull(executor.getWatchdog(), "watchdog"));
  }

}