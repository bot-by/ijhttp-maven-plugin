# Usage of the Maven plugin

**Important!** The plugin does not contain the HTTP client: you need to install it by yourself
then add to `PATH`. You can also set the full path to the ijhttp via the parameter `executable`.
The [HTTP Client Demo][demo] has some examples how to download the HTTP client.

[![Maven Central](https://img.shields.io/maven-central/v/uk.bot-by.ijhttp-tools/ijhttp-maven-plugin)](https://search.maven.org/artifact/uk.bot-by.ijhttp-tools/ijhttp-maven-plugin)
[![Javadoc](https://javadoc.io/badge2/uk.bot-by.ijhttp-tools/ijhttp-maven-plugin/javadoc.svg)](https://javadoc.io/doc/uk.bot-by.ijhttp-tools/ijhttp-maven-plugin)

There is one goal **run**.
To manage plugin's output use `useMavenLogger`, `quietLogs` and `outputFile`.

## Parameters

The parameters equal to arguments of `ijhttp`. Run `ijhttp --help` to learn them.

- **connectTimeout** - Number of milliseconds for connection. Defaults to _3000_.
- **directories** - Directories to look up HTTP files. At least one `file` or `directory` is required.
- **dockerMode** - Enables Docker mode. Treat `localhost` as `host.docker.internal`. Defaults to _false_.
- **environmentFile** - Name of the public environment file, e.g. `http-client.env.json`.
- **environmentName** - Name of the environment in a configuration file.
- **environmentVariables** - Public environment variables.
  Example:
  ```xml
  ...
  <environmentVariables>
    <environmentVariable>id=1234</environmentVariable>
    <environmentVariable>field=name</environmentVariable>
  </environmentVariables>
  ...
  ```
- **executable** - The executable. Can be a full path or the name of the executable. Defaults to _ijhttp_.
- **files** - HTTP file paths.
  Example:
  ```xml
  ...
  <files>
    <file>simple-run.http</file>
  </files>
  ...
  ```
- **insecure** - Allow insecure SSL connection. Defaults to _false_.
- **logLevel** - Logging level: `BASIC`, `HEADERS`, `VERBOSE`. Defaults to _BASIC_.
- **outputFile** - Program standard and error output will be redirected to the file specified
  by this optional field.
- **privateEnvironmentFile** - Name of the private environment file,
  e.g. `http-client.private.env.json`.
- **privateEnvironmentVariables** - Private environment variables.
- **proxy** - Proxy URI. Proxy setting in format `scheme://login:password@host:port`,
  _scheme_ can be _socks_ for SOCKS or _http_ for HTTP.
- **quietLog** - When combined with `useMavenLogger=true`, prints all executed program
  output at `DEBUG` level instead of the default `INFO` level to the Maven logger.
- **report** - Creates report about execution in JUnit XML Format. Puts it in folder `reports`
  in the current directory. Defaults to _false_.
- **reportPath** - Path to a report folder. Default value {@code reports } in the current directory.
- **skip** - Skip the execution. Defaults to _false_.
- **socketTimeout** - Number of milliseconds for socket read. Defaults to _10000_.
- **timeout** - Number of milliseconds for execution.
- **useMavenLogger** - When enabled, program standard and error output will be redirected
  to the Maven logger as `INFO` and `ERROR` level logs, respectively.
- **workingDirectory** - The working directory. This is optional: if not specified, the current
  directory will be used.

## Example of configuration

```xml
<plugin>
  <groupId>io.gitlab.vitalijr2.ijhttp-tools</groupId>
  <artifactId>ijhttp-maven-plugin</artifactId>
  <version><!-- search on Maven Central --></version>
  <executions>
    <execution>
      <configuration>
       <directories>
         <directory>src/test/resources</directory>
       </directories>
       <environmentFile>public-env.json</environmentFile>
       <environmentName>dev</environmentName>
       <files>
         <file>sample-1-queries.http</file>
         <file>sample-2-queries.http</file>
       </files>
       <logLevel>HEADERS</logLevel>
       <report>true</report>
       <workingDirectory>target</workingDirectory>
      </configuration>
      <goals>
        <goal>run</goal>
      </goals>
      <id>simple-run-with-report</id>
    </execution>
  </executions>
</plugin>
```

[demo]: https://gitlab.com/vitalijr2/ijhttp-demo