/*
 * Copyright 2023-2024 bot-by
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.bot_by.ijhttp_tools.junit_extension;

import static java.util.Objects.nonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import uk.bot_by.ijhttp_tools.command_line.HttpClientCommandLine;
import uk.bot_by.ijhttp_tools.command_line.LogLevel;

class HttpClientCommandLineResolver implements ParameterResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientCommandLineResolver.class);

  private static void copyBooleanParametersAndLogLevelAndExecutable(
      HttpClientCommandLineParameters parameters, HttpClientCommandLine httpClientCommandLine) {
    httpClientCommandLine.dockerMode(parameters.dockerMode());
    httpClientCommandLine.executable(parameters.executable());
    httpClientCommandLine.insecure(parameters.insecure());
    httpClientCommandLine.logLevel(LogLevel.valueOf(parameters.logLevel()));
    httpClientCommandLine.report(parameters.report());
  }

  private static void handleEnvironment(HttpClientCommandLineParameters parameters,
      HttpClientCommandLine httpClientCommandLine) {
    if (nonNull(parameters.environmentFile())) {
      httpClientCommandLine.environmentFile(Path.of(parameters.environmentFile()));
    }
    if (nonNull(parameters.environmentName())) {
      httpClientCommandLine.environmentName(parameters.environmentName());
    }
    if (nonNull(parameters.environmentVariables())) {
      httpClientCommandLine.environmentVariables(List.of(parameters.environmentVariables()));
    }
    if (nonNull(parameters.privateEnvironmentFile())) {
      httpClientCommandLine.privateEnvironmentFile(Path.of(parameters.privateEnvironmentFile()));
    }
    if (nonNull(parameters.privateEnvironmentVariables())) {
      httpClientCommandLine.privateEnvironmentVariables(
          List.of(parameters.privateEnvironmentVariables()));
    }
  }

  private static void handleFileParameters(HttpClientCommandLineParameters parameters,
      HttpClientCommandLine httpClientCommandLine) {
    if (nonNull(parameters.files())) {
      httpClientCommandLine.files(Stream.of(parameters.files()).map(Path::of).toArray(Path[]::new));
    }
    if (nonNull(parameters.directories())) {
      httpClientCommandLine.directories(
          Stream.of(parameters.directories()).map(Path::of).toArray(Path[]::new));
    }
    if (nonNull(parameters.reportPath())) {
      httpClientCommandLine.reportPath(Path.of(parameters.reportPath()));
    }
  }

  private static void handleProxy(HttpClientCommandLineParameters parameters,
      HttpClientCommandLine httpClientCommandLine) {
    if (nonNull(parameters.proxy())) {
      httpClientCommandLine.proxy(parameters.proxy());
    }
  }

  private static void handleTimeout(HttpClientCommandLineParameters parameters,
      HttpClientCommandLine httpClientCommandLine) {
    if (0 < parameters.connectTimeout()) {
      httpClientCommandLine.connectTimeout(parameters.connectTimeout());
    }
    if (0 < parameters.socketTimeout()) {
      httpClientCommandLine.socketTimeout(parameters.socketTimeout());
    }
  }

  private static HttpClientCommandLine httpClientCommandLine(
      HttpClientCommandLineParameters parameters) {
    LOGGER.debug(() -> String.format("HTTP Client parameters %s", parameters));

    var httpClientCommandLine = new HttpClientCommandLine();

    copyBooleanParametersAndLogLevelAndExecutable(parameters, httpClientCommandLine);
    handleEnvironment(parameters, httpClientCommandLine);
    handleFileParameters(parameters, httpClientCommandLine);
    handleProxy(parameters, httpClientCommandLine);
    handleTimeout(parameters, httpClientCommandLine);

    return httpClientCommandLine;
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return HttpClientCommandLine.class == parameterContext.getParameter().getType()
        && parameterContext.isAnnotated(HttpClientCommandLineParameters.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    var parameters = parameterContext.getAnnotatedElement()
        .getAnnotation(HttpClientCommandLineParameters.class);

    return httpClientCommandLine(parameters);
  }

}
