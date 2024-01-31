# Usage of the jUnit Extension

**Important!** The extension does not contain the HTTP client: you need to install it by yourself
then add to `PATH`. You can also set the full path to the ijhttp via the parameter `executable`.
The [HTTP Client Demo][demo] has some examples how to download the HTTP client.

[![Maven Central](https://img.shields.io/maven-central/v/uk.bot-by.ijhttp-tools/ijhttp-junit-extension)](https://search.maven.org/artifact/uk.bot-by.ijhttp-tools/ijhttp-junit-extension)
[![Javadoc](https://javadoc.io/badge2/uk.bot-by.ijhttp-tools/ijhttp-junit-extension/javadoc.svg)](https://javadoc.io/doc/uk.bot-by.ijhttp-tools/ijhttp-junit-extension)

Use annotations `HttpClientExecutor` and `HttpClientCommandLineParameters` to initialise and
configure both executor and command line builder.

The parameters equal to arguments of `ijhttp`. Run `ijhttp --help` to learn them.

- **connect-timeout** - Number of milliseconds for connection. Defaults to _3000_.
- **directories** - Directories to look up HTTP files. At least one `file` or `directory` is required.
- **docker-mode** - Enables Docker mode. Treat `localhost` as `host.docker.internal`. Defaults to _false_.
- **environment-file** - Name of the public environment file, e.g. `http-client.env.json`.
- **environment-name** - Name of the environment in a configuration file.
- **environment-variables** - Public environment variables.
- **executable** - The executable. Can be a full path or the name of the executable. Defaults to _ijhttp_.
- **files** - HTTP file paths.
- **insecure** - Allow insecure SSL connection. Defaults to _false_.
- **log-level** - Logging level: `BASIC`, `HEADERS`, `VERBOSE`. Defaults to _BASIC_.
- **private-environment-file** - Name of the private environment file,
  e.g. `http-client.private.env.json`.
- **private-environment-variables** - Private environment variables.
- **proxy** - Proxy URI. Proxy setting in format `scheme://login:password@host:port`,
  _scheme_ can be _socks_ for SOCKS or _http_ for HTTP.
- **report** - Creates report about execution in JUnit XML Format. Puts it in folder `reports`
  in the current directory. Defaults to _false_.
- **report-path** - Path to a report folder. Default value {@code reports } in the current directory.
- **socket-timeout** - Number of milliseconds for socket read. Defaults to _10000_.
- **timeout** - Number of milliseconds for execution.

## Example of configuration

```java
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureHttpClientCommandLine(timeout = 7000)
class HttpClientCommandLineApplicationTests {

  @Autowired
  private Executor executor;

  @Autowired
  private HttpClientCommandLine httpClientCommandLine;

  @Test
  void httpClientCommandLine() throws IOException {
    // when
    var exitCode = executor.execute(httpClientCommandLine.getCommandLine());

    // then
    assertEquals(0, exitCode);
  }

}
```

[demo]: https://gitlab.com/vitalijr2/ijhttp-demo