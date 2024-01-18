# Maven plugin and Spring Boot Test autoconfiguration for IntelliJ HTTP Client

I had started with a Maven plugin to run HTTP requests on the <em>integration-test</em> phase
using the [IntelliJ HTTP Client][http-client]. Later I added a Spring Boot Test autoconfiguration,
thanks [@GoncaloPT][GoncaloPT] for [his idea][leverage-test].

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/73e1f8501ed84b0580dcf7ccee82c1e0)](https://app.codacy.com/gl/bot-by/ijhttp-maven-plugin/dashboard?utm_source=gl&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Coverage](https://app.codacy.com/project/badge/Coverage/73e1f8501ed84b0580dcf7ccee82c1e0)](https://app.codacy.com/gl/bot-by/ijhttp-maven-plugin/dashboard?utm_source=gl&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)
[![Java Version](https://img.shields.io/static/v1?label=java&message=17&color=blue&logo=java&logoColor=E23D28)](https://www.oracle.com/java/technologies/downloads/#java17)

## Getting started

Originally the IntelliJ HTTP Client plugin allows to create, edit, and execute HTTP requests
directly in the IntelliJ IDEA code editor.
The IntelliJ HTTP Client is also [available as a CLI tool][cli-tool].

The plugin allows to run HTTP requests on the <em>integration-test</em> phase
using the IntelliJ HTTP Client. The autoconfiguration allows to run them with Spring Boot Test,
you don't need to package and run whole application.

The [HTTP Request in Editor Specification][specification]
describes format these files.

Example requests:

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

### Directories (extra feature)

**IntelliJ HTTP Client** needs HTTP files to work.
With **HTTP Client Command Line** you can set directories that contain such files.

## Usage

* [Maven plugin][maven-plugin]
* [Spring Boot Test autoconfiguration][autoconfiguration]

[http-client]: https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html

[GoncaloPT]: https://github.com/GoncaloPT "Gonçalo Silva"

[leverage-test]: https://github.com/bot-by/ijhttp-maven-plugin/issues/51 "Leverage test instead of using main app"

[cli-tool]: https://www.jetbrains.com/help/idea/http-client-cli.html

[specification]: https://github.com/JetBrains/http-request-in-editor-spec

[maven-plugin]: maven-plugin.html

[autoconfiguration]: autoconfiguration.html
