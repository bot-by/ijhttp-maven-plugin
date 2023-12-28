/*
 * Copyright 2023 Vitalij Berdinskih
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
package uk.bot_by.ijhttp_tools.spring_boot_test;

import java.nio.file.Path;
import java.util.List;
import java.util.StringJoiner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import uk.bot_by.ijhttp_tools.command_line.LogLevel;

/**
 * HTTP Client parameters.
 *
 * @see <a href="https://www.jetbrains.com/help/idea/http-client-cli.html">HTTP Client CLI</a>
 * @since 1.1.0
 */
@ConfigurationProperties(prefix = "ijhttp.parameters")
public class HttpClientCommandLineParameters {

  /**
   * Number of milliseconds for connection. Defaults to <em>3000</em>.
   */
  private Integer connectTimeout;
  /**
   * Directories to look up HTTP files. At least one {@code file} or {@code directory} is required.
   */
  private List<Path> directories;
  /**
   * Enables Docker mode. Treat <em>localhost</em> as <em>host.docker.internal</em>. Defaults to
   * <em>false</em>.
   */
  private boolean dockerMode;
  /**
   * Name of the public environment file, e.g. <em>http-client.env.json</em>.
   */
  private Path environmentFile;
  /**
   * Public environment variables.
   */
  private List<String> environmentVariables;
  /**
   * Name of the environment in a configuration file.
   */
  private String environmentName;
  /**
   * The executable. Can be a full path or the name of the executable. Defaults to
   * <em>ijhttp</em>.
   */
  private String executable = "ijhttp";
  /**
   * HTTP file paths. At least one {@code file} or {@code directory} is required.
   */
  private List<Path> files;
  /**
   * Allow insecure SSL connection. Defaults to <em>false</em>.
   */
  private boolean insecure;
  /**
   * Logging level: BASIC, HEADERS, VERBOSE. Defaults to <em>BASIC</em>.
   */
  private LogLevel logLevel = LogLevel.BASIC;
  /**
   * Name of the private environment file, e.g. <em>http-client.private.env.json</em>.
   */
  private Path privateEnvironmentFile;
  /**
   * Private environment variables.
   */
  private List<String> privateEnvironmentVariables;
  /**
   * Proxy URI.
   * <p>
   * Proxy setting in format <em>scheme://login:password@host:port</em>, <em>scheme<em> can be
   * <em>socks<em> for <strong>SOCKS</strong> or <em>http<em> for <strong>HTTP</strong>.
   */
  private String proxy;
  /**
   * Creates report about execution in JUnit XML Format. Defaults to <em>false</em>.
   */
  private boolean report;
  /**
   * Path to a report folder. Default value <em>reports</em> in the current directory.
   */
  private Path reportPath;
  /**
   * Number of milliseconds for socket read. Defaults to <em>10000</em>.
   */
  private Integer socketTimeout;

  public Integer getConnectTimeout() {
    return connectTimeout;
  }

  /**
   * Number of milliseconds for connection. Defaults to <em>3000</em>.
   */
  public void setConnectTimeout(Integer connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public List<Path> getDirectories() {
    return directories;
  }

  /**
   * Directories to look up HTTP files. At least one {@code file} or {@code directory} is required.
   *
   * @see #setFiles(List)
   * @since 1.2.0
   */
  public void setDirectories(List<Path> directories) {
    this.directories = directories;
  }

  public boolean isDockerMode() {
    return dockerMode;
  }

  /**
   * Enables Docker mode. Treat <em>localhost</em> as <em>host.docker.internal</em>. Defaults to
   * <em>false</em>.
   */
  public void setDockerMode(boolean dockerMode) {
    this.dockerMode = dockerMode;
  }

  public Path getEnvironmentFile() {
    return environmentFile;
  }

  /**
   * Name of the public environment file.
   */
  public void setEnvironmentFile(Path environmentFile) {
    this.environmentFile = environmentFile;
  }

  public List<String> getEnvironmentVariables() {
    return environmentVariables;
  }

  /**
   * Public environment variables.
   */
  public void setEnvironmentVariables(List<String> environmentVariables) {
    this.environmentVariables = environmentVariables;
  }

  public String getEnvironmentName() {
    return environmentName;
  }

  /**
   * Name of the environment in a configuration file.
   */
  public void setEnvironmentName(String environmentName) {
    this.environmentName = environmentName;
  }

  public String getExecutable() {
    return executable;
  }

  /**
   * The executable. Can be a full path or the name of the executable. Defaults to
   * <em>ijhttp</em>.
   */
  public void setExecutable(String executable) {
    this.executable = executable;
  }

  public List<Path> getFiles() {
    return files;
  }

  /**
   * HTTP file paths. At least one {@code file} or {@code directory} is required.
   *
   * @see #setDirectories(List)
   * @since 1.2.0
   */
  public void setFiles(List<Path> files) {
    this.files = files;
  }

  public boolean isInsecure() {
    return insecure;
  }

  /**
   * Allow insecure SSL connection. Defaults to <em>false</em>.
   */
  public void setInsecure(boolean insecure) {
    this.insecure = insecure;
  }

  public LogLevel getLogLevel() {
    return logLevel;
  }

  /**
   * Logging level: BASIC, HEADERS, VERBOSE. Defaults to <em>BASIC</em>.
   */
  public void setLogLevel(LogLevel logLevel) {
    this.logLevel = logLevel;
  }

  public Path getPrivateEnvironmentFile() {
    return privateEnvironmentFile;
  }

  /**
   * Name of the private environment file.
   */
  public void setPrivateEnvironmentFile(Path privateEnvironmentFile) {
    this.privateEnvironmentFile = privateEnvironmentFile;
  }

  public List<String> getPrivateEnvironmentVariables() {
    return privateEnvironmentVariables;
  }

  /**
   * Private environment variables.
   */
  public void setPrivateEnvironmentVariables(List<String> privateEnvironmentVariables) {
    this.privateEnvironmentVariables = privateEnvironmentVariables;
  }

  public String getProxy() {
    return proxy;
  }

  /**
   * Proxy URI.
   * <p>
   * Proxy setting in format <em>scheme://login:password@host:port</em>, <em>scheme<em> can be
   * <em>socks<em> for <strong>SOCKS</strong> or <em>http<em> for <strong>HTTP</strong>.
   */
  public void setProxy(String proxy) {
    this.proxy = proxy;
  }

  public boolean isReport() {
    return report;
  }

  /**
   * Creates report about execution in JUnit XML Format. Defaults to <em>false</em>.
   */
  public void setReport(boolean report) {
    this.report = report;
  }

  public Path getReportPath() {
    return reportPath;
  }

  /**
   * Path to a report folder. Default value <em>reports</em> in the current directory.
   */
  public void setReportPath(Path reportPath) {
    this.reportPath = reportPath;
  }

  public Integer getSocketTimeout() {
    return socketTimeout;
  }

  /**
   * Number of milliseconds for socket read. Defaults to <em>10000</em>.
   */
  public void setSocketTimeout(Integer socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", getClass().getSimpleName() + "[", "]").add(
            "connectTimeout=" + connectTimeout).add("directories=" + directories)
        .add("dockerMode=" + dockerMode).add("environmentFile=" + environmentFile)
        .add("environmentVariables=" + environmentVariables)
        .add("environmentName='" + environmentName + "'").add("executable='" + executable + "'")
        .add("files=" + files).add("insecure=" + insecure).add("logLevel=" + logLevel)
        .add("privateEnvironmentFile=" + privateEnvironmentFile)
        .add("privateEnvironmentVariables=" + privateEnvironmentVariables)
        .add("proxy='" + proxy + "'").add("report=" + report).add("reportPath=" + reportPath)
        .add("socketTimeout=" + socketTimeout).toString();
  }
}