# HTTP Client Maven Plugin

[![Maven Central](https://img.shields.io/maven-central/v/uk.bot-by.ijhttp-tools/ijhttp-maven-plugin)](https://search.maven.org/artifact/uk.bot-by.ijhttp-tools/ijhttp-maven-plugin)
[![Javadoc](https://javadoc.io/badge2/uk.bot-by.ijhttp-tools/ijhttp-maven-plugin/javadoc.svg)](https://javadoc.io/doc/uk.bot-by.ijhttp-tools/ijhttp-maven-plugin)

The plugin allows to run HTTP requests on the <em>integration-test</em> phase
using the [IntelliJ HTTP Client][http-client]. The [HTTP Request in Editor Specification][specification]
describes format these files.

Example of test request:

```language-apex
GET https://example.com/api/get

### Add an item
POST https://example.com/api/add
Content-Type: application/json

{
  "name": "entity",
  "value": "content"
}
```

## Configuration

**Important!** The plugin does not contain the [HTTP client][cli-tool]:
you need to install it by yourself then add to `PATH`. You can also set the full path to the ijhttp
via the parameter `executable`. The [HTTP Client Demo][demo] has some examples how to download
the HTTP client.

There is one goal **run**. To use it add the plugin to your POM.

Example of full configuration:

```language-xml
<plugin>
  <groupId>uk.bot-by.ijhttp-tools</groupId>
  <artifactId>ijhttp-maven-plugin</artifactId>
  <version><!-- search on Maven Central --></version>
  <executions>
    <execution>
      <configuration>
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

To manage plugin's output use `useMavenLogger`, `quietLogs` and `outputFile`.

You can play with [HTTP Client Demo][demo].

[http-client]: https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html

[specification]: https://github.com/JetBrains/http-request-in-editor-spec

[cli-tool]: https://www.jetbrains.com/help/idea/http-client-cli.html

[demo]: https://gitlab.com/vitalijr2/ijhttp-demo