/*
 * Copyright 2023-2024 Vitalij Berdinskih
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
package io.gitlab.vitalij_r2.ijhttp_tools.command_line;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import org.apache.commons.exec.CommandLine;
import org.jetbrains.annotations.NotNull;

/**
 * HTTP Client command line parameters.
 * <p>
 * The minimal configuration contains HTTP files only:
 * <pre><code class="language-java">
 * var commandLine = new HttpClientCommandLine();
 * var executor = new DefaultExecutor();
 * var orders = Path.of("orders.http");
 * var products = Path.of("products.http");
 * var checkout = Path.of("checkout.http");
 *
 * commandLine.files(orders, products, checkout);
 * executor.execute(commandLine.getCommandLine());
 * </code></pre>
 * <p>
 * IntelliJ HTTP Client uses <code>--report</code> as boolean option and parameter with a file
 * value. The component implements it by two methods: {@link #report(boolean)} and
 * {@link #reportPath(Path)}.
 *
 * @author Vitalij Berdinskih
 * @since 1.1.0
 */
public class HttpClientCommandLine {

  // Request files
  private static final String HTTP_EXTENSION = ".http";
  private static final String REST_EXTENSION = ".rest";
  private static final BiPredicate<Path, BasicFileAttributes> REQUEST_FILE = ((path, attributes) ->
      Files.isRegularFile(path) && (path.getFileName().toString().endsWith(HTTP_EXTENSION)
          || path.getFileName().toString().endsWith(REST_EXTENSION)));

  // Parameters
  private static final String CONNECT_TIMEOUT = "--connect-timeout";
  private static final String DOCKER_MODE = "--docker-mode";
  private static final String ENV = "--env";
  private static final String ENV_FILE = "--env-file";
  private static final String ENV_VARIABLES = "--env-variables";
  private static final String INSECURE = "--insecure";
  private static final String LOG_LEVEL = "--log-level";
  private static final String PRIVATE_ENV_FILE = "--private-env-file";
  private static final String PRIVATE_ENV_VARIABLES = "--private-env-variables";
  private static final String PROXY = "--proxy";
  private static final String REPORT = "--report";
  private static final String SOCKET_TIMEOUT = "--socket-timeout";

  private Integer connectTimeout;
  private Path[] directories = new Path[0];
  private boolean dockerMode;
  private Path environmentFile;
  private List<String> environmentVariables;
  private String environmentName;
  private String executable = "ijhttp";
  private Path[] files = new Path[0];
  private boolean insecure;
  private LogLevel logLevel = LogLevel.BASIC;
  private int maxDepth = Integer.MAX_VALUE;
  private Path privateEnvironmentFile;
  private List<String> privateEnvironmentVariables;
  private String proxy;
  private boolean report;
  private Path reportPath;
  private Integer socketTimeout;

  /**
   * Number of milliseconds for connection. Defaults to <em>3000</em>.
   */
  public void connectTimeout(@NotNull Integer connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  /**
   * Directories to look up HTTP files. At least one {@code file} or {@code directory} is required.
   *
   * @see #files(Path...)
   * @since 1.2.0
   */
  public void directories(@NotNull Path... directories) {
    this.directories = directories;
  }

  /**
   * Enables Docker mode. Treat {@code localhost} as {@code host.docker.internal}. Defaults to
   * <em>false</em>.
   */
  public void dockerMode(boolean dockerMode) {
    this.dockerMode = dockerMode;
  }

  /**
   * Name of the public environment file, e.g. {@code http-client.env.json}.
   */
  public void environmentFile(@NotNull Path environmentFile) {
    this.environmentFile = environmentFile;
  }

  /**
   * Public environment variable.
   */
  public void environmentVariable(@NotNull String environmentVariable) {
    synchronized (this) {
      if (isNull(environmentVariables)) {
        environmentVariables = new ArrayList<>();
      }
    }
    environmentVariables.add(environmentVariable);
  }

  /**
   * Public environment variables.
   */
  public void environmentVariables(@NotNull List<String> environmentVariables) {
    this.environmentVariables = environmentVariables;
  }

  /**
   * Name of the environment in a configuration file.
   */
  public void environmentName(@NotNull String environmentName) {
    this.environmentName = environmentName;
  }

  /**
   * The executable. Can be a full path or the name of the executable. Defaults to {@code ijhttp}.
   */
  public void executable(@NotNull String executable) {
    this.executable = executable;
  }

  /**
   * HTTP file paths. At least one {@code file} or {@code directory} is required.
   *
   * @see #directories(Path...)
   */
  public void files(Path... files) {
    this.files = files;
  }

  /**
   * Allow insecure SSL connection. Defaults to <em>false</em>.
   */
  public void insecure(boolean insecure) {
    this.insecure = insecure;
  }

  /**
   * Logging level: BASIC, HEADERS, VERBOSE. Defaults to <em>BASIC</em>.
   */
  public void logLevel(@NotNull LogLevel logLevel) {
    this.logLevel = logLevel;
  }

  /**
   * The maximum depth of a directory tree to traverse. Default value {@link Integer#MAX_VALUE}.
   */
  public void maxDepth(int maxDepth) {
    this.maxDepth = maxDepth;
  }

  /**
   * Name of the private environment file, e.g. {@code http-client.private.env.json}.
   */
  public void privateEnvironmentFile(@NotNull Path privateEnvironmentFile) {
    this.privateEnvironmentFile = privateEnvironmentFile;
  }

  /**
   * Private environment variable.
   */
  public void privateEnvironmentVariable(@NotNull String privateEnvironmentVariable) {
    synchronized (this) {
      if (isNull(privateEnvironmentVariables)) {
        privateEnvironmentVariables = new ArrayList<>();
      }
    }
    privateEnvironmentVariables.add(privateEnvironmentVariable);
  }

  /**
   * Private environment variables.
   *
   * @see #environmentVariables
   */
  public void privateEnvironmentVariables(@NotNull List<String> privateEnvironmentVariables) {
    this.privateEnvironmentVariables = privateEnvironmentVariables;
  }

  /**
   * Proxy URI.
   * <p>
   * Proxy setting in format {@code scheme://login:password@host:port}, <em>scheme<em> can be
   * <em>socks<em> for SOCKS or <em>http<em> for HTTP.
   */
  public void proxy(@NotNull String proxy) {
    this.proxy = proxy;
  }

  /**
   * Creates report about execution in JUnit XML Format. Defaults to <em>false</em>.
   *
   * @see #reportPath(Path)
   */
  public void report(boolean report) {
    this.report = report;
  }

  /**
   * Path to a report folder. Default value {@code reports } in the current directory.
   *
   * @see #report(boolean)
   */
  public void reportPath(@NotNull Path reportPath) {
    this.reportPath = reportPath;
  }

  /**
   * Number of milliseconds for socket read. Defaults to <em>10000</em>.
   */
  public void socketTimeout(@NotNull Integer socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  /**
   * Get command line.
   *
   * @return command line
   * @throws IllegalArgumentException if HTTP files are missed
   * @throws IOException              if path to HTTP or environment files or report directory is
   *                                  wrong
   */
  public CommandLine getCommandLine() throws IllegalArgumentException, IOException {
    var commandLine = new CommandLine(executable);

    if (0 == files.length && 0 == directories.length) {
      throw new IllegalStateException("files are required");
    }
    flags(commandLine);
    logLevel(commandLine);
    timeouts(commandLine);
    environmentName(commandLine);
    environment(commandLine);
    privateEnvironment(commandLine);
    proxy(commandLine);
    requests(commandLine);
    // As workaround for IDEA-339395
    // https://youtrack.jetbrains.com/issue/IDEA-339395/HTTP-Client-CLI-order-of-the-parameter-report-interferes-on-interpretation-of-other-parameters
    report(commandLine);

    return commandLine;
  }

  private void environment(CommandLine commandLine) throws IOException {
    if (nonNull(environmentFile)) {
      commandLine.addArgument(ENV_FILE).addArgument(environmentFile.toString());
    }
    if (nonNull(environmentVariables)) {
      environmentVariables.forEach(
          variable -> commandLine.addArgument(ENV_VARIABLES).addArgument(variable, false));
    }
  }

  private void environmentName(CommandLine commandLine) {
    if (nonNull(environmentName) && !environmentName.isBlank()) {
      commandLine.addArgument(ENV).addArgument(environmentName);
    }
  }

  private void flags(CommandLine commandLine) throws IOException {
    if (dockerMode) {
      commandLine.addArgument(DOCKER_MODE);
    }
    if (insecure) {
      commandLine.addArgument(INSECURE);
    }
  }

  private void logLevel(CommandLine commandLine) {
    switch (logLevel) {
      case HEADERS:
      case VERBOSE:
        commandLine.addArgument(LOG_LEVEL).addArgument(logLevel.name());
      case BASIC:
      default:
        // do nothing
    }
  }

  private void privateEnvironment(CommandLine commandLine) throws IOException {
    if (nonNull(privateEnvironmentFile)) {
      commandLine.addArgument(PRIVATE_ENV_FILE).addArgument(privateEnvironmentFile.toString());
    }
    if (nonNull(privateEnvironmentVariables)) {
      privateEnvironmentVariables.forEach(
          variable -> commandLine.addArgument(PRIVATE_ENV_VARIABLES).addArgument(variable, false));
    }
  }

  private void proxy(CommandLine commandLine) {
    if (nonNull(proxy)) {
      commandLine.addArgument(PROXY).addArgument(proxy, false);
    }
  }

  private void report(CommandLine commandLine) throws IOException {
    if (report) {
      commandLine.addArgument(REPORT);
      if (nonNull(reportPath)) {
        commandLine.addArgument(reportPath.toString(), false);
      }
    }
  }

  private void requests(CommandLine commandLine) throws IOException {
    for (Path file : files) {
      commandLine.addArgument(file.toString());
    }
    for (Path directory : directories) {
      try (Stream<Path> pathStream = Files.find(directory, maxDepth, REQUEST_FILE)) {
        for (Path path : pathStream.toList()) {
          commandLine.addArgument(path.toString());
        }
      }
    }
  }

  private void timeouts(CommandLine commandLine) {
    if (nonNull(connectTimeout)) {
      commandLine.addArgument(CONNECT_TIMEOUT).addArgument(connectTimeout.toString());
    }
    if (nonNull(socketTimeout)) {
      commandLine.addArgument(SOCKET_TIMEOUT).addArgument(socketTimeout.toString());
    }
  }

}