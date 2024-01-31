# ijhttp tools: Spring Boot Test autoconfiguration

[![Maven Central](https://img.shields.io/maven-central/v/uk.bot-by.ijhttp-tools/ijhttp-spring-boot-test)](https://search.maven.org/artifact/uk.bot-by.ijhttp-tools/ijhttp-spring-boot-test)
[![Javadoc](https://javadoc.io/badge2/uk.bot-by.ijhttp-tools/ijhttp-spring-boot-test/javadoc.svg)](https://javadoc.io/doc/uk.bot-by.ijhttp-tools/ijhttp-spring-boot-test)

The Spring Boot Test autoconfiguration allows to run them with Spring Boot Test using
the [IntelliJ HTTP Client][http-client], you don't need to package and run whole application.

The [HTTP Request in Editor Specification][specification] describes format these files.

Example of test request:

```http
GET /api/get HTTP/1.1
Accept: application/json
Host: example.com

### Add an item
POST /api/add HTTP/1.1
Host: example.com
Content-Type: application/json

{
  "name": "entity",
  "value": "content"
}
```

## Configuration

**Important!** The autoconfiguration does not contain the [HTTP client][cli-tool]:
you need to install it by yourself then add to `PATH`. You can also set the full path to the ijhttp
via the parameter `executable`. The [HTTP Client Demo][demo] has some examples how to download
the HTTP client.

Example of autoconfiguration, full configuration:

```yaml
ijhttp:
  parameters:
    connect-timeout: 9000
    directories:
      - src/test/resources/ijhttp
    # docker-mode: false default value
    environment-file: public-env.json
    environment-name: dev
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
  # timeout: 7000
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

You can play with [HTTP Client Demo][demo].

[http-client]: https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html

[specification]: https://github.com/JetBrains/http-request-in-editor-spec

[cli-tool]: https://www.jetbrains.com/help/idea/http-client-cli.html

[demo]: https://gitlab.com/vitalijr2/ijhttp-demo