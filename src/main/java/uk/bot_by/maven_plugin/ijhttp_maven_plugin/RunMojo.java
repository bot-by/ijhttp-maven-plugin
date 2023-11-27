/*
 * Copyright 2023 Vitalij Berdinskih
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Run integration tests using IntelliJ HTTP Client.
 * <p>
 * Sample configuration:
 * <pre><code class="language-xml">
 *   &lt;configuration&gt;
 *     &lt;environmentFile&gt;public-env.json&lt;/environmentFile&gt;
 *     &lt;environmentName&gt;dev&lt;/environmentName&gt;
 *     &lt;files&gt;
 *       &lt;file&gt;sample-1-queries.http&lt;/file&gt;
 *       &lt;file&gt;sample-2-queries.http&lt;/file&gt;
 *     &lt;/files&gt;
 *     &lt;logLevel&gt;HEADERS&lt;/logLevel&gt;
 *     &lt;report&gt;true&lt;/report&gt;
 *     &lt;workingDirectory&gt;target&lt;/workingDirectory&gt;
 *   &lt;/configuration&gt;
 * </code></pre>
 * Environment variables:
 * <pre><code class="language-xml">
 *   ...
 *   &lt;environmentVariables&gt;
 *     &lt;environmentVariable&gt;id=1234&lt;/environmentVariable&gt;
 *     &lt;environmentVariable&gt;field=name&lt;/environmentVariable&gt;
 *   &lt;/environmentVariables&gt;
 *   ...
 * </code></pre>
 * To manage plugin's output use {@link #useMavenLogger}, {@link #quietLogs} and
 * {@link #outputFile}.
 *
 * @author Vitalij Berdinskih
 * @see <a href="https://www.jetbrains.com/help/idea/http-client-cli.html">HTTP Client CLI</a>
 * @see <a href="https://www.youtube.com/live/mwiHAukbWjM?feature=share">Lve stream: The New HTTP
 * Client CLI</a>
 * @since 1.0.0
 */
@Mojo(name = "run", defaultPhase = LifecyclePhase.INTEGRATION_TEST, requiresProject = false)
public class RunMojo extends AbstractMojo {

  private static final String CONNECT_TIMEOUT = "--connect-timeout";
  private static final String DOCKER_MODE = "--docker-mode";
  private static final String ENV_FILE = "--env-file";
  private static final String ENV = "--env";
  private static final String ENV_VARIABLES = "--env-variables";
  private static final String INSECURE = "--insecure";
  private static final String LOG_LEVEL = "--log-level";
  private static final String PRIVATE_ENV_FILE = "--private-env-file";
  private static final String PRIVATE_ENV_VARIABLES = "--private-env-variables";
  private static final String PROXY = "--proxy";
  private static final String REPORT = "--report";
  private static final String SOCKET_TIMEOUT = "--socket-timeout";

  private Integer connectTimeout;
  private boolean dockerMode;
  private File environmentFile;
  private List<String> environmentVariables;
  private String environmentName;
  private String executable;
  private List<File> files;
  private boolean insecure;
  private LogLevel logLevel;
  private File outputFile;
  private File privateEnvironmentFile;
  private List<String> privateEnvironmentVariables;
  private String proxy;
  private boolean quietLogs;
  private boolean report;
  private boolean skip;
  private Integer socketTimeout;
  private Integer timeout;
  private boolean useMavenLogger;
  private File workingDirectory;

  /**
   * Run HTTP requests.
   *
   * @throws MojoExecutionException if a error happens
   * @throws MojoFailureException   if a failure happens
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (skip) {
      getLog().info("skipping execute as per configuration");
      return;
    }

    try {
      var commandLine = getCommandLine();
      var executor = getExecutor();

      if (getLog().isDebugEnabled()) {
        getLog().debug("Executing command line: " + commandLine);
      }
      try {
        runHttpClient(commandLine, executor);
      } catch (ExecuteException exception) {
        if (nonNull(executor.getWatchdog()) && executor.getWatchdog().killedProcess()) {
          var message = "Timeout. Process runs longer that " + timeout + " ms.";

          getLog().error(message);
          throw new MojoExecutionException(message);
        } else {
          var message = "Execution failed: " + exception.getMessage();

          getLog().error(message, exception);
          throw new MojoExecutionException(message, exception);
        }
      }
    } catch (IOException exception) {
      var message = new StringBuilder("I/O Error");

      if (nonNull(exception.getMessage()) && !exception.getMessage().isBlank()) {
        message.append(": ").append(exception.getMessage());
      }
      getLog().warn(message);
      throw new MojoExecutionException(message.toString(), exception);
    }
  }

  /**
   * Number of milliseconds for connection. Defaults to <em>3000</em>.
   */
  @Parameter(property = "ijhttp.connect-timeout")
  public void setConnectTimeout(Integer connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  /**
   * Enables Docker mode. Treat {@code localhost} as {@code host.docker.internal}. Defaults to
   * <em>false</em>.
   */
  @Parameter(property = "ijhttp.docker-mode", defaultValue = "false")
  public void setDockerMode(boolean dockerMode) {
    this.dockerMode = dockerMode;
  }

  /**
   * Name of the public environment file, e.g. {@code http-client.env.json}.
   */
  @Parameter(property = "ijhttp.env-file")
  public void setEnvironmentFile(File environmentFile) {
    this.environmentFile = environmentFile;
  }

  /**
   * Public environment variables.
   * <p>
   * Example:
   * <pre><code class="language-xml">
   *   &lt;configuration&gt;
   *     &lt;environmentVariables&gt;
   *       &lt;environmentVariable&gt;id=1234&lt;/environmentVariable&gt;
   *       &lt;environmentVariable&gt;field=name&lt;/environmentVariable&gt;
   *     &lt;/environmentVariables&gt;
   *   &lt;/configuration&gt;
   * </code></pre>
   */
  @Parameter(property = "ijhttp.env-variables")
  public void setEnvironmentVariables(List<String> environmentVariables) {
    this.environmentVariables = environmentVariables;
  }

  /**
   * Name of the environment in a configuration file.
   */
  @Parameter(property = "ijhttp.env")
  public void setEnvironmentName(String environmentName) {
    this.environmentName = environmentName;
  }

  /**
   * The executable. Can be a full path or the name of the executable.
   */
  @Parameter(property = "ijhttp.executable", defaultValue = "ijhttp")
  public void setExecutable(String executable) {
    this.executable = executable;
  }

  /**
   * HTTP file paths. They are required.
   * <p>
   * Example:
   * <pre><code class="language-xml">
   *   &lt;files&gt;
   *     &lt;file&gt;simple-run.http&lt;/file&gt;
   *   &lt;/files&gt;
   * </code></pre>
   */
  @Parameter(property = "ijhttp.files", required = true)
  public void setFiles(List<File> files) {
    this.files = files;
  }

  /**
   * Allow insecure SSL connection. Defaults to <em>false</em>.
   */
  @Parameter(property = "ijhttp.insecure", defaultValue = "false")
  public void setInsecure(boolean insecure) {
    this.insecure = insecure;
  }

  /**
   * Logging level: BASIC, HEADERS, VERBOSE. Defaults to <em>BASIC</em>.
   */
  @Parameter(property = "ijhttp.log-level", defaultValue = "BASIC")
  public void setLogLevel(LogLevel logLevel) {
    this.logLevel = logLevel;
  }

  /**
   * Program standard and error output will be redirected to the file specified by this optional
   * field. If not enabled the traditional behavior of program output being directed to standard
   * {@code System.out} and {@code System.err} is used.
   *
   * @see #setUseMavenLogger(boolean)
   */
  @Parameter(property = "ijhttp.output-file")
  public void setOutputFile(File outputFile) {
    this.outputFile = outputFile;
  }

  /**
   * Name of the private environment file, e.g. {@code http-client.private.env.json}.
   */
  @Parameter(property = "ijhttp.private-env-file")
  public void setPrivateEnvironmentFile(File privateEnvironmentFile) {
    this.privateEnvironmentFile = privateEnvironmentFile;
  }

  /**
   * Private environment variables.
   *
   * @see #environmentVariables
   */
  @Parameter(property = "ijhttp.private-env-variables")
  public void setPrivateEnvironmentVariables(List<String> privateEnvironmentVariables) {
    this.privateEnvironmentVariables = privateEnvironmentVariables;
  }

  /**
   * Proxy URI.
   * <p>
   * Proxy setting in format {@code scheme://login:password@host:port}, <em>scheme<em> can be
   * <em>socks<em> for SOCKS or <em>http<em> for HTTP.
   */
  @Parameter(property = "ijhttp.proxy")
  public void setProxy(String proxy) {
    this.proxy = proxy;
  }

  /**
   * When combined with {@code ijhttp.useMavenLogger=true}, prints all executed program output at
   * debug level instead of the default info level to the Maven logger.
   *
   * @see #setOutputFile(File)
   * @see #setUseMavenLogger(boolean)
   */
  @Parameter(property = "ijhttp.quietLogs", defaultValue = "false")
  public void setQuietLogs(boolean quietLogs) {
    this.quietLogs = quietLogs;
  }

  /**
   * Creates report about execution in JUnit XML Format. Puts it in folder {@code reports } in the
   * current directory. Defaults to <em>false</em>.
   */
  @Parameter(property = "ijhttp.report", defaultValue = "false")
  public void setReport(boolean report) {
    this.report = report;
  }

  /**
   * Skip the execution. Defaults to <em>false</em>.
   */
  @Parameter(property = "ijhttp.skip", defaultValue = "false")
  public void setSkip(boolean skip) {
    this.skip = skip;
  }

  /**
   * Number of milliseconds for socket read. Defaults to <em>10000</em>.
   */
  @Parameter(property = "ijhttp.socket-timeout")
  public void setSocketTimeout(Integer socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  /**
   * Number of milliseconds for execution.
   */
  @Parameter(property = "ijhttp.timeout")
  public void setTimeout(Integer timeout) {
    this.timeout = timeout;
  }

  /**
   * When enabled, program standard and error output will be redirected to the Maven logger as
   * <em>Info</em> and <em>Error</em> level logs, respectively. If not enabled the traditional
   * behavior of program output being directed to standard {@code System.out} and {@code System.err}
   * is used.
   *
   * @see #setOutputFile(File)
   * @see #setQuietLogs(boolean)
   */
  @Parameter(property = "ijhttp.useMavenLogger", defaultValue = "false")
  public void setUseMavenLogger(boolean useMavenLogger) {
    this.useMavenLogger = useMavenLogger;
  }

  /**
   * The working directory. Defaults to <em>${basedir}</em>.
   */
  @Parameter(property = "ijhttp.workingdir", defaultValue = "${basedir}")
  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  @VisibleForTesting
  CommandLine getCommandLine() throws IOException, MojoExecutionException {
    var commandLine = new CommandLine(executable);

    if (isNull(files)) {
      throw new MojoExecutionException("files are required");
    }
    flags(commandLine);
    logLevel(commandLine);
    timeouts(commandLine);
    environmentName(commandLine);
    environment(commandLine);
    privateEnvironment(commandLine);
    proxy(commandLine);
    requests(commandLine);

    return commandLine;
  }

  @VisibleForTesting
  Executor getExecutor() throws IOException, MojoExecutionException {
    var executor = new DefaultExecutor();

    handleWatchdog(executor);
    handleWorkingDirectory(executor);

    return executor;
  }

  private void environment(CommandLine commandLine) throws IOException {
    if (nonNull(environmentFile)) {
      commandLine.addArgument(ENV_FILE).addArgument(environmentFile.getCanonicalPath());
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

  private void flags(CommandLine commandLine) {
    if (dockerMode) {
      commandLine.addArgument(DOCKER_MODE);
    }
    if (insecure) {
      commandLine.addArgument(INSECURE);
    }
    if (report) {
      commandLine.addArgument(REPORT);
    }
  }

  private void handleWatchdog(DefaultExecutor executor) {
    if (nonNull(timeout)) {
      var watchdog = new ExecuteWatchdog(timeout);

      executor.setWatchdog(watchdog);
      if (getLog().isDebugEnabled()) {
        getLog().debug(String.format("Set the watchdog (%s) ms", timeout));
      }
    }
  }

  private void handleWorkingDirectory(DefaultExecutor executor)
      throws IOException, MojoExecutionException {
    if (nonNull(workingDirectory)) {
      if (!workingDirectory.exists()) {
        Files.createDirectory(workingDirectory.toPath());
      } else if (!workingDirectory.isDirectory()) {
        throw new MojoExecutionException(
            "the working directory is a file: " + workingDirectory.getPath());
      }
      executor.setWorkingDirectory(workingDirectory);
    }
    if (getLog().isDebugEnabled()) {
      getLog().debug("Working directory: " + executor.getWorkingDirectory());
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
      commandLine.addArgument(PRIVATE_ENV_FILE)
          .addArgument(privateEnvironmentFile.getCanonicalPath());
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

  private void requests(CommandLine commandLine) throws IOException {
    for (File file : files) {
      commandLine.addArgument(file.getCanonicalPath());
    }
  }

  private void runHttpClient(CommandLine commandLine, Executor executor) throws IOException {
    if (nonNull(outputFile)) {
      if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
        getLog().warn(
            "Could not create non existing parent directories for the log file: " + outputFile);
      }
      var outputStream = new FileOutputStream(outputFile);
      executor.setStreamHandler(new PumpStreamHandler(new BufferedOutputStream(outputStream)));
      if (getLog().isDebugEnabled()) {
        getLog().debug("Will redirect program output to the log file: " + outputFile);
      }
      try (outputStream) {
        executor.getStreamHandler().start();
        executor.execute(commandLine);
      } finally {
        executor.getStreamHandler().stop();
      }
    } else if (useMavenLogger) {
      var loggerErrStream = new LogOutputStream() {
        @Override
        protected void processLine(String line, int logLevel) {
          getLog().error(line);
        }
      };
      var loggerOutStream = new LogOutputStream() {
        @Override
        protected void processLine(String line, int logLevel) {
          if (quietLogs) {
            getLog().debug(line);
          } else {
            getLog().info(line);
          }
        }
      };
      executor.setStreamHandler(new PumpStreamHandler(loggerOutStream, loggerErrStream));
      if (getLog().isDebugEnabled()) {
        getLog().debug("Will redirect program output to Maven logger");
      }
      try (loggerErrStream; loggerOutStream) {
        executor.getStreamHandler().start();
        executor.execute(commandLine);
      } finally {
        executor.getStreamHandler().stop();
      }
    } else {
      executor.setStreamHandler(new PumpStreamHandler());
      try {
        executor.getStreamHandler().start();
        executor.execute(commandLine);
      } finally {
        executor.getStreamHandler().stop();
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

  /**
   * Logging levels.
   */
  public enum LogLevel {
    /**
     * Print out HTTP request filename, names and values of public variables, names of private
     * variables, names and URLs of requests.
     */
    BASIC,
    /**
     * Add to BASIC level HTTP headers.
     */
    HEADERS,
    /**
     * Add to HEADERS level request and response bodies, execution statistics.
     */
    VERBOSE
  }

}
