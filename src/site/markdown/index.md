# IntelliJ HTTP Client, Maven Plugin

A Maven Plugin  to run HTTP requests through the [IntelliJ HTTP Client][http-client] on
the _integration-test_ phase.

[![Codacy Grade](https://app.codacy.com/project/badge/Grade/73e1f8501ed84b0580dcf7ccee82c1e0)](https://app.codacy.com/gl/bot-by/ijhttp-maven-plugin/dashboard?utm_source=gl&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Coverage](https://app.codacy.com/project/badge/Coverage/73e1f8501ed84b0580dcf7ccee82c1e0)](https://app.codacy.com/gl/bot-by/ijhttp-maven-plugin/dashboard?utm_source=gl&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)

## Getting started

Originally the HTTP Client plugin allows to create, edit, and execute HTTP requests directly in the
IntelliJ IDEA code editor. The HTTP Client is also [available as a CLI tool][cli-tool].

The plugin allows to run HTTP requests through the IntelliJ HTTP Client on
the _integration-test_ phase. The [HTTP Request in Editor Specification][specification]
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

* [Usage][]
* [Configuration][]

[http-client]: https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html

[cli-tool]: https://www.jetbrains.com/help/idea/http-client-cli.html

[specification]: https://github.com/JetBrains/http-request-in-editor-spec

[Usage]: usage.html

[Configuration]: configuration.html
