/*
 * Copyright 2023 Witalij Berdinskich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.bot_by.maven_plugin.ijhttp_maven_plugin;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Run integration tests using IntelliJ HTTP Client.
 *
 * @author Witalij Berdinskich
 * @see <a href="https://www.jetbrains.com/help/idea/http-client-cli.html">HTTP Client CLI</a>
 */
@Mojo(name = "integration-test", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.INTEGRATION_TEST)
public class IntegrationTestMojo extends AbstractMojo {

  private static final String CONNECT_TIMEOUT = "--connect-timeout";
  private static final String DOCKER_MODE = "--docker-mode";
  private static final String ENV_FILE = "--env-file";
  private static final String ENVIRONMENT_NAME = "--env";
  private static final String ENV_VARIABLES = "--env-variables";
  private static final String EXECUTABLE = "ijhttp";
  private static final String INSECURE = "--insecure";
  private static final String LOG_LEVEL = "--log-level";
  private static final String PRIVATE_ENV_FILE = "--private-env-file";
  private static final String PRIVATE_ENV_VARIABLES = "--private-env-variables";
  private static final String REPORT = "--report";
  private static final String SOCKET_TIMEOUT = "--socket-timeout";

  public enum LogLevel {BASIC, HEADERS, VERBOSE}

  @Parameter(property = "ijhttp.connect-timeout")
  private Integer connectTimeout;

  @Parameter(property = "ijhttp.docker-mode")
  private boolean dockerMode;

  @Parameter(property = "ijhttp.env-file")
  private File environmentFile;

  @Parameter(property = "ijhttp.env-variables")
  private List<String> environmentVariables;

  @Parameter(property = "ijhttp.env")
  private String environmentName;

  @Parameter(property = "ijhttp.files", required = true)
  private List<File> files;

  @Parameter(property = "ijhttp.insecure", defaultValue = "false")
  private boolean insecure;

  @Parameter(property = "ijhttp.log-level", defaultValue = "BASIC")
  private LogLevel logLevel;

  @Parameter(property = "ijhttp.private-env-file")
  private File privateEnvironmentFile;

  @Parameter(property = "ijhttp.private-env-variables")
  private List<String> privateEnvironmentVariables;

  @Parameter(property = "ijhttp.report", defaultValue = "false")
  private boolean report;

  @Parameter(property = "ijhttp.skip", defaultValue = "false")
  private boolean skip;

  @Parameter(property = "ijhttp.socket-timeout")
  private Integer socketTimeout;

  public IntegrationTestMojo() {
  }

  @VisibleForTesting
  IntegrationTestMojo(Integer connectTimeout, boolean dockerMode, File environmentFile,
      List<String> environmentVariables, String environmentName, List<File> files, boolean insecure,
      LogLevel logLevel, File privateEnvironmentFile, List<String> privateEnvironmentVariables,
      boolean report, boolean skip, Integer socketTimeout) {
    this.connectTimeout = connectTimeout;
    this.dockerMode = dockerMode;
    this.environmentFile = environmentFile;
    this.environmentVariables = environmentVariables;
    this.environmentName = environmentName;
    this.files = files;
    this.insecure = insecure;
    this.logLevel = logLevel;
    this.privateEnvironmentFile = privateEnvironmentFile;
    this.privateEnvironmentVariables = privateEnvironmentVariables;
    this.report = report;
    this.skip = skip;
    this.socketTimeout = socketTimeout;
  }

  @Override
  public void execute() throws MojoExecutionException {
    if (skip) {
      getLog().info("skipping execute as per configuration");
      return;
    }

    var executor = new DefaultExecutor();

    try {
      var commandLine = getCommandLine();

      executor.execute(commandLine);
    } catch (IOException exception) {
      throw new MojoExecutionException("I/O Error", exception);
    }
  }

  @VisibleForTesting
  CommandLine getCommandLine() throws IOException {
    var commandLine = new CommandLine(EXECUTABLE);

    if (nonNull(connectTimeout)) {
      commandLine.addArgument(CONNECT_TIMEOUT).addArgument(connectTimeout.toString());
    }
    if (dockerMode) {
      commandLine.addArgument(DOCKER_MODE);
    }
    if (nonNull(environmentFile)) {
      commandLine.addArgument(ENV_FILE).addArgument(environmentFile.getCanonicalPath());
    }
    if (nonNull(environmentName) && !environmentName.isBlank()) {
      commandLine.addArgument(ENVIRONMENT_NAME).addArgument(environmentName);
    }
    if (nonNull(environmentVariables)) {
      environmentVariables.forEach(
          variable -> commandLine.addArgument(ENV_VARIABLES).addArgument(variable));
    }
    if (insecure) {
      commandLine.addArgument(INSECURE);
    }
    switch (logLevel) {
      case HEADERS:
      case VERBOSE:
        commandLine.addArgument(LOG_LEVEL).addArgument(logLevel.name());
      case BASIC:
      default:
        // do nothing
    }
    if (nonNull(privateEnvironmentFile)) {
      commandLine.addArgument(PRIVATE_ENV_FILE)
          .addArgument(privateEnvironmentFile.getCanonicalPath());
    }
    if (nonNull(privateEnvironmentVariables)) {
      privateEnvironmentVariables.forEach(
          variable -> commandLine.addArgument(PRIVATE_ENV_VARIABLES).addArgument(variable));
    }
    if (report) {
      commandLine.addArgument(REPORT);
    }
    if (nonNull(socketTimeout)) {
      commandLine.addArgument(SOCKET_TIMEOUT).addArgument(socketTimeout.toString());
    }
    if (isNull(files)) {
      throw new IllegalArgumentException("files are required");
    }
    for (File file : files) {
      commandLine.addArgument(file.getCanonicalPath());
    }

    return commandLine;
  }

}
