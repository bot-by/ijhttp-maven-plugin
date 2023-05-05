# Usage

**Important!** The plugin does not contain the HTTP client: you need to install it by yourself
then add to `PATH`. You can also set the full path to the ijhttp via the parameter `executable`.

[![Maven Central](https://img.shields.io/maven-central/v/uk.bot-by/ijhttp-maven-plugin)](https://search.maven.org/artifact/uk.bot-by/ijhttp-maven-plugin)
[![Javadoc](https://javadoc.io/badge2/uk.bot-by/ijhttp-maven-plugin/javadoc.svg)](https://javadoc.io/doc/uk.bot-by/ijhttp-maven-plugin)

There is one goal **run**. Add the plugin to your POM, example of full configuration:

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

Further reading: [Configuration][].

[Configuration]: configuration.html