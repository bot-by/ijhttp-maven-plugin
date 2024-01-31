# Usage of the jUnit Extension

**Important!** The extension does not contain the HTTP client: you need to install it by yourself
then add to `PATH`. You can also set the full path to the ijhttp via the parameter `executable`.
The [HTTP Client Demo][demo] has some examples how to download the HTTP client.

[![Maven Central](https://img.shields.io/maven-central/v/uk.bot-by.ijhttp-tools/ijhttp-junit-extension)](https://search.maven.org/artifact/uk.bot-by.ijhttp-tools/ijhttp-junit-extension)
[![Javadoc](https://javadoc.io/badge2/uk.bot-by.ijhttp-tools/ijhttp-junit-extension/javadoc.svg)](https://javadoc.io/doc/uk.bot-by.ijhttp-tools/ijhttp-junit-extension)

Use annotations `HttpClientExecutor` and `HttpClientCommandLineParameters` to initialise and
configure both executor and command line builder.
