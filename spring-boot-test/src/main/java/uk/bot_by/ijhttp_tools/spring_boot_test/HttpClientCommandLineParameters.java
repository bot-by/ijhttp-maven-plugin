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
package uk.bot_by.ijhttp_tools.spring_boot_test;

import java.io.File;
import java.util.List;
import java.util.StringJoiner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import uk.bot_by.ijhttp_tools.command_line.LogLevel;

@ConfigurationProperties(prefix = "ijhttp.parameters")
public class HttpClientCommandLineParameters {

  private Integer connectTimeout;
  private boolean dockerMode;
  private File environmentFile;
  private List<String> environmentVariables;
  private String environmentName;
  private String executable = "ijhttp";
  private List<File> files;
  private boolean insecure;
  private LogLevel logLevel = LogLevel.BASIC;
  private File privateEnvironmentFile;
  private List<String> privateEnvironmentVariables;
  private String proxy;
  private boolean report;
  private File reportPath;
  private Integer socketTimeout;

  public Integer getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(Integer connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public boolean isDockerMode() {
    return dockerMode;
  }

  public void setDockerMode(boolean dockerMode) {
    this.dockerMode = dockerMode;
  }

  public File getEnvironmentFile() {
    return environmentFile;
  }

  public void setEnvironmentFile(File environmentFile) {
    this.environmentFile = environmentFile;
  }

  public List<String> getEnvironmentVariables() {
    return environmentVariables;
  }

  public void setEnvironmentVariables(List<String> environmentVariables) {
    this.environmentVariables = environmentVariables;
  }

  public String getEnvironmentName() {
    return environmentName;
  }

  public void setEnvironmentName(String environmentName) {
    this.environmentName = environmentName;
  }

  public String getExecutable() {
    return executable;
  }

  public void setExecutable(String executable) {
    this.executable = executable;
  }

  public List<File> getFiles() {
    return files;
  }

  public void setFiles(List<File> files) {
    this.files = files;
  }

  public boolean isInsecure() {
    return insecure;
  }

  public void setInsecure(boolean insecure) {
    this.insecure = insecure;
  }

  public LogLevel getLogLevel() {
    return logLevel;
  }

  public void setLogLevel(LogLevel logLevel) {
    this.logLevel = logLevel;
  }

  public File getPrivateEnvironmentFile() {
    return privateEnvironmentFile;
  }

  public void setPrivateEnvironmentFile(File privateEnvironmentFile) {
    this.privateEnvironmentFile = privateEnvironmentFile;
  }

  public List<String> getPrivateEnvironmentVariables() {
    return privateEnvironmentVariables;
  }

  public void setPrivateEnvironmentVariables(List<String> privateEnvironmentVariables) {
    this.privateEnvironmentVariables = privateEnvironmentVariables;
  }

  public String getProxy() {
    return proxy;
  }

  public void setProxy(String proxy) {
    this.proxy = proxy;
  }

  public boolean isReport() {
    return report;
  }

  public void setReport(boolean report) {
    this.report = report;
  }

  public File getReportPath() {
    return reportPath;
  }

  public void setReportPath(File reportPath) {
    this.reportPath = reportPath;
  }

  public Integer getSocketTimeout() {
    return socketTimeout;
  }

  public void setSocketTimeout(Integer socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", getClass().getSimpleName() + "[", "]").add(
            "connectTimeout=" + connectTimeout).add("dockerMode=" + dockerMode)
        .add("environmentFile=" + environmentFile)
        .add("environmentVariables=" + environmentVariables)
        .add("environmentName='" + environmentName + "'").add("executable='" + executable + "'")
        .add("files=" + files).add("insecure=" + insecure).add("logLevel=" + logLevel)
        .add("privateEnvironmentFile=" + privateEnvironmentFile)
        .add("privateEnvironmentVariables=" + privateEnvironmentVariables)
        .add("proxy='" + proxy + "'").add("report=" + report).add("reportPath=" + reportPath)
        .add("socketTimeout=" + socketTimeout).toString();
  }

}