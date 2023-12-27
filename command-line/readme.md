# HTTP Client Command Line

[![Maven Central](https://img.shields.io/maven-central/v/uk.bot-by.ijhttp-tools/ijhttp-command-line)](https://search.maven.org/artifact/uk.bot-by.ijhttp-tools/ijhttp-command-line)
[![Javadoc](https://javadoc.io/badge2/uk.bot-by.ijhttp-tools/ijhttp-command-line/javadoc.svg)](https://javadoc.io/doc/uk.bot-by.ijhttp-tools/ijhttp-command-line)

The builder-style component [HttpClientCommandLine][component] helps to prepare command line
to run [Intellij HTTP Client CLI tool][cli-tool].

The minimal configuration contains HTTP files only:
```java
var commandLine = new HttpClientCommandLine();
var executor = new DefaultExecutor();
var orders = Path.of("orders/orders.http");
var products = Path.of("catalog/products.http");
var checkout = Path.of("orders/checkout.http");

commandLine.files(orders, products, checkout);
executor.execute(commandLine.getCommandLine());
```

## Directories

**IntelliJ HTTP Client** needs HTTP files to work.
With **HTTP Client Command Line** you can set directories that contain such files.

The same example as above but with directories looks like:
```java
var commandLine = new HttpClientCommandLine();
var executor = new DefaultExecutor();
var orders = Path.of("orders");
var catalog = Path.of("catalog");

commandLine.directories(orders, catalog);
executor.execute(commandLine.getCommandLine());
```

[component]: src/main/java/uk/bot_by/ijhttp_tools/command_line/HttpClientCommandLine.java

[cli-tool]: https://www.jetbrains.com/help/idea/http-client-cli.html