# Usage

**Important!** The plugin does not contain the HTTP client: you need to install it by yourself then add to `PATH`.

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