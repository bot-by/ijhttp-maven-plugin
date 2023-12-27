package uk.bot_by.ijhttp_tools.spring_boot_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import uk.bot_by.ijhttp_tools.command_line.LogLevel;

@Tag("fast")
class HttpClientCommandLineParametersTest {

  @DisplayName("Default values")
  @Test
  void defaultValues() {
    // given
    var parameters = new HttpClientCommandLineParameters();

    // when and then
    assertEquals("HttpClientCommandLineParameters[connectTimeout=null, directories=null, "
            + "dockerMode=false, environmentFile=null, environmentVariables=null, "
            + "environmentName='null', executable='ijhttp', files=null, insecure=false, "
            + "logLevel=BASIC, privateEnvironmentFile=null, privateEnvironmentVariables=null, "
            + "proxy='null', report=false, reportPath=null, socketTimeout=null]",
        parameters.toString());
  }

  @DisplayName("Custom values")
  @Test
  void customValues() throws IOException {
    // given
    var file = mock(Path.class);
    var parameters = new HttpClientCommandLineParameters();

    parameters.setDirectories(List.of(Path.of("test-directory")));
    parameters.setDockerMode(true);
    parameters.setExecutable("test.sh");
    parameters.setInsecure(true);
    parameters.setLogLevel(LogLevel.VERBOSE);
    parameters.setReport(true);

    parameters.setConnectTimeout(1);
    parameters.setEnvironmentFile(file);
    parameters.setEnvironmentName("name");
    parameters.setEnvironmentVariables(List.of("public"));
    parameters.setFiles(List.of(file));
    parameters.setPrivateEnvironmentFile(file);
    parameters.setPrivateEnvironmentVariables(List.of("private"));
    parameters.setProxy("proxy");
    parameters.setReportPath(file);
    parameters.setSocketTimeout(2);

    when(file.toString()).thenReturn("path");

    // when and then
    assertEquals("HttpClientCommandLineParameters[connectTimeout=1, "
        + "directories=[test-directory], dockerMode=true, environmentFile=path, "
        + "environmentVariables=[public], environmentName='name', executable='test.sh', "
        + "files=[path], insecure=true, logLevel=VERBOSE, privateEnvironmentFile=path, "
        + "privateEnvironmentVariables=[private], proxy='proxy', report=true, reportPath=path, "
        + "socketTimeout=2]", parameters.toString());
  }

}