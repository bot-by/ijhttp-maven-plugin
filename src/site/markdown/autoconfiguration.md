# Srping Boot Test autoconfiguration

**Important!** The autoconfiguration does not contain the HTTP client: you need to install it
by yourself then add to `PATH`. You can also set the full path to the ijhttp
via the parameter `executable`.
The [HTTP Client Demo][demo] has some examples how to download the HTTP client.

[![Maven Central](https://img.shields.io/maven-central/v/uk.bot-by.ijhttp-tools/ijhttp-spring-boot-test)](https://search.maven.org/artifact/uk.bot-by.ijhttp-tools/ijhttp-spring-boot-test)
[![Javadoc](https://javadoc.io/badge2/uk.bot-by.ijhttp-tools/ijhttp-spring-boot-test/javadoc.svg)](https://javadoc.io/doc/uk.bot-by.ijhttp-tools/ijhttp-spring-boot-test)

The parameters equal to arguments of `ijhttp`. Run `ijhttp --help` to learn them.

- **connect-timeout** - Number of milliseconds for connection. Defaults to _3000_.
- **docker-mode** - Enables Docker mode. Treat `localhost` as `host.docker.internal`. Defaults to _false_.
- **environment-file** - Name of the public environment file, e.g. `http-client.env.json`.
- **environment-name** - Name of the environment in a configuration file.
- **environment-variables** - Public environment variables.
- **executable** - The executable. Can be a full path or the name of the executable. Defaults to _ijhttp_.
- **files** - HTTP file paths. They are required.
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

```yaml
ijhttp:
  # timeout: 7000 same as annotation's property 'timeout'
  parameters:
    connect-timeout: 9000
    # docker-mode: false default value
    environment-file: public-env.json
    environment-name: dev
    environment-variables:
      - id=1234
      - field=name
    # executable: ijhttp default value
    files:
      - orders.http
      - products.http
      - checkout.http
    # insecure: false default value
    log-level: verbose
    private-environment-file: private-env.json
    # proxy: http://localhost:3128/
    report: true
    report-path: target/ijhttp
    socket-timeout: 9000
```

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