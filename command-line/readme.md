# IntelliJ HTTP Client Command Line

The builder-style component [HttpClientCommandLine][component] helps to prepare command line
to run [Intellij HTTP Client CLI tool][cli-tool].

The minimal configuration contains HTTP files only:
```java
var commandLine = new HttpClientCommandLine();
var executor = new DefaultExecutor();
var files = Path.of("orders.http").toFile();
var products = Path.of("products.http").toFile();
var checkout = Path.of("checkout.http").toFile();

commandLine.files(java.util.List.of(files, products, checkout));
executor.execute(commandLine.getCommandLine());
```

[component]: src/main/java/uk/bot_by/ijhttp_tools/command_line/HttpClientCommandLine.java

[cli-tool]: https://www.jetbrains.com/help/idea/http-client-cli.html