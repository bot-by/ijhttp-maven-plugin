# IntelliJ HTTP Client Maven Plugin

A Maven Plugin  to run HTTP requests through the [IntelliJ HTTP Client][http-client] on
the <em>integration-test</em> phase.

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/73e1f8501ed84b0580dcf7ccee82c1e0)](https://app.codacy.com/gl/bot-by/ijhttp-maven-plugin/dashboard?utm_source=gl&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Coverage](https://app.codacy.com/project/badge/Coverage/73e1f8501ed84b0580dcf7ccee82c1e0)](https://app.codacy.com/gl/bot-by/ijhttp-maven-plugin/dashboard?utm_source=gl&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)
[![Java Version](https://img.shields.io/static/v1?label=java&message=17&color=blue&logo=java&logoColor=E23D28)](https://www.oracle.com/java/technologies/downloads/#java17)

Table of Contents
=================

* [Getting started](#getting-started)
* [Usage](#usage)
* [Contributing](#contributing)
* [History](#history)
* [License](#license)

Created by [gh-md-toc](https://github.com/ekalinin/github-markdown-toc)

## Getting started

Originally the HTTP Client plugin allows to create, edit, and execute HTTP requests directly in the
IntelliJ IDEA code editor. The HTTP Client is also [available as a CLI tool][cli-tool].

The plugin allows to run HTTP requests through the IntelliJ HTTP Client on
the <em>integration-test</em> phase. The [HTTP Request in Editor Specification][specification]
describes format these files.

Example requests:

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

## Usage

**Important!** The plugin does not contain the HTTP client: you need to install it by yourself
then add to `PATH`. You can also set the full path to the ijhttp via the parameter `executable`.

[![Maven Central](https://img.shields.io/maven-central/v/uk.bot-by/ijhttp-maven-plugin)](https://search.maven.org/artifact/uk.bot-by/ijhttp-maven-plugin)
[![Javadoc](https://javadoc.io/badge2/uk.bot-by/ijhttp-maven-plugin/javadoc.svg)](https://javadoc.io/doc/uk.bot-by/ijhttp-maven-plugin)

There is one goal **run**. To use it add the plugin to your POM.

Example of full configuration:

```language-xml
<plugin>
  <groupId>uk.bot-by.maven-plugin</groupId>
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

## Contributing

Please read [Contributing](contributing.md).

## History

See [Changelog](changelog.md)

## License

Copyright 2023 Witalij Berdinskich

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[Apache License v2.0](LICENSE)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)

[http-client]: https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html

[cli-tool]: https://www.jetbrains.com/help/idea/http-client-cli.html

[specification]: https://github.com/JetBrains/http-request-in-editor-spec

