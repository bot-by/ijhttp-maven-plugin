# HTTP Client Command Line

[![Maven Central](https://img.shields.io/maven-central/v/uk.bot-by.ijhttp-tools/ijhttp-command-line)](https://search.maven.org/artifact/uk.bot-by.ijhttp-tools/ijhttp-command-line)
[![Javadoc](https://javadoc.io/badge2/uk.bot-by.ijhttp-tools/ijhttp-command-line/javadoc.svg)](https://javadoc.io/doc/uk.bot-by.ijhttp-tools/ijhttp-command-line)

The builder-style component [HttpClientCommandLine][component] helps to prepare command line
to run [Intellij HTTP Client CLI tool][cli-tool].

The minimal configuration contains HTTP files only:
```java
var commandLine = new HttpClientCommandLine();
var executor = new DefaultExecutor();
var orders = Path.of("orders.http").toFile();
var products = Path.of("products.http").toFile();
var checkout = Path.of("checkout.http").toFile();

commandLine.files(java.util.List.of(orders, products, checkout));
executor.execute(commandLine.getCommandLine());
```

[component]: src/main/java/uk/bot_by/ijhttp_tools/command_line/HttpClientCommandLine.java

[cli-tool]: https://www.jetbrains.com/help/idea/http-client-cli.html