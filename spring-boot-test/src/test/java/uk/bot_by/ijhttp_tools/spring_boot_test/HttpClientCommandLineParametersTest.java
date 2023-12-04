package uk.bot_by.ijhttp_tools.spring_boot_test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpClientCommandLineParametersTest {

  @DisplayName("toString()")
  @Test
  void stringValue() {
    // given
    var parameters = new HttpClientCommandLineParameters();

    // when and then
    assertEquals("HttpClientCommandLineParameters[connectTimeout=null, dockerMode=false, "
        + "environmentFile=null, environmentVariables=null, environmentName='null', "
        + "executable='ijhttp', files=null, insecure=false, logLevel=BASIC, "
        + "privateEnvironmentFile=null, privateEnvironmentVariables=null, proxy='null', "
        + "report=false, reportPath=null, socketTimeout=null]", parameters.toString());
  }

}