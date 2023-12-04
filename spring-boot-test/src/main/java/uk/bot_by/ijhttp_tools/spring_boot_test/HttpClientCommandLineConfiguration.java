package uk.bot_by.ijhttp_tools.spring_boot_test;

import static java.util.Objects.nonNull;

import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import uk.bot_by.ijhttp_tools.command_line.HttpClientCommandLine;

@ConditionalOnWebApplication(type = Type.SERVLET)
@EnableConfigurationProperties(HttpClientCommandLineParameters.class)
public class HttpClientCommandLineConfiguration {

  private final Logger logger = LoggerFactory.getLogger(HttpClientCommandLineConfiguration.class);

  @Bean
  @ConditionalOnMissingBean
  Executor executor(@Value("${ijhttp.timeout:0}") int timeout) {
    var executor = new DefaultExecutor();

    if (timeout > 0) {
      executor.setWatchdog(new ExecuteWatchdog(timeout));
      if (logger.isDebugEnabled()) {
        logger.debug(String.format("Set the watchdog (%s) ms", timeout));
      }
    }

    return executor;
  }

  @Bean
  HttpClientCommandLine httpClientCommandLine(HttpClientCommandLineParameters parameters) {
    logger.debug("HTTP Client parameters {}", parameters);

    var httpClientCommandLine = new HttpClientCommandLine();

    copyBooleanParametersAndLogLevelAndExecutable(parameters, httpClientCommandLine);
    handleEnvironment(parameters, httpClientCommandLine);
    handleFileParameters(parameters, httpClientCommandLine);
    handleProxy(parameters, httpClientCommandLine);
    handleTimeout(parameters, httpClientCommandLine);

    return httpClientCommandLine;
  }

  private void copyBooleanParametersAndLogLevelAndExecutable(
      HttpClientCommandLineParameters parameters, HttpClientCommandLine httpClientCommandLine) {
    httpClientCommandLine.dockerMode(parameters.isDockerMode());
    httpClientCommandLine.executable(parameters.getExecutable());
    httpClientCommandLine.insecure(parameters.isInsecure());
    httpClientCommandLine.logLevel(parameters.getLogLevel());
    httpClientCommandLine.report(parameters.isReport());
  }

  private static void handleEnvironment(HttpClientCommandLineParameters parameters,
      HttpClientCommandLine httpClientCommandLine) {
    if (nonNull(parameters.getEnvironmentFile())) {
      httpClientCommandLine.environmentFile(parameters.getEnvironmentFile());
    }
    if (nonNull(parameters.getEnvironmentName())) {
      httpClientCommandLine.environmentName(parameters.getEnvironmentName());
    }
    if (nonNull(parameters.getEnvironmentVariables())) {
      httpClientCommandLine.environmentVariables(parameters.getEnvironmentVariables());
    }
    if (nonNull(parameters.getPrivateEnvironmentFile())) {
      httpClientCommandLine.privateEnvironmentFile(parameters.getPrivateEnvironmentFile());
    }
    if (nonNull(parameters.getPrivateEnvironmentVariables())) {
      httpClientCommandLine.privateEnvironmentVariables(
          parameters.getPrivateEnvironmentVariables());
    }
  }

  private static void handleFileParameters(HttpClientCommandLineParameters parameters,
      HttpClientCommandLine httpClientCommandLine) {
    if (nonNull(parameters.getFiles())) {
      httpClientCommandLine.files(parameters.getFiles());
    }
    if (nonNull(parameters.getReportPath())) {
      httpClientCommandLine.reportPath(parameters.getReportPath());
    }
  }

  private static void handleProxy(HttpClientCommandLineParameters parameters,
      HttpClientCommandLine httpClientCommandLine) {
    if (nonNull(parameters.getProxy())) {
      httpClientCommandLine.proxy(parameters.getProxy());
    }
  }

  private static void handleTimeout(HttpClientCommandLineParameters parameters,
      HttpClientCommandLine httpClientCommandLine) {
    if (nonNull(parameters.getConnectTimeout())) {
      httpClientCommandLine.connectTimeout(parameters.getConnectTimeout());
    }
    if (nonNull(parameters.getSocketTimeout())) {
      httpClientCommandLine.socketTimeout(parameters.getSocketTimeout());
    }
  }

}
