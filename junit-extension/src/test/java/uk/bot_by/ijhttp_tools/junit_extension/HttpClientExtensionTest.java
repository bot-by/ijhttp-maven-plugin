package uk.bot_by.ijhttp_tools.junit_extension;

import org.apache.commons.exec.Executor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(HttpClientExtension.class)
class HttpClientExtensionTest {

  @HttpClientExecutor
  private static String staticField;
  private String field;
  @HttpClientExecutor
  private Executor executor;

  @Test
  void test(@HttpClientExecutor Executor executor) {
  }

}