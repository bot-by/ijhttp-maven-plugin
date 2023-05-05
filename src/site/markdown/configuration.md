# Configuration

The parameters equal to arguments of `ijhttp`. Run `ijhttp --help` to learn them.

- **connectTimeout** - Number of milliseconds for connection. Defaults to _3000_.
- **dockerMode** - Enables Docker mode. Treat `localhost` as `host.docker.internal`. Defaults to _false_.
- **environmentFile** - Name of the public environment file, e.g. `http-client.env.json`.
- **environmentVariables** - Public environment variables.
  Example:
  ```language-xml
  ...
  <environmentVariables>
    <environmentVariable>id=1234</environmentVariable>
    <environmentVariable>field=name</environmentVariable>
  </environmentVariables>
  ...
  ```
- **environmentName** - Name of the environment in a configuration file.
- **executable** - The executable. Can be a full path or the name of the executable. Defaults to _ijhttp_.
- **files** - HTTP file paths. They are required.
  Example:
  ```language-xml
  ...
  <files>
    <file>simple-run.http</file>
  </files>
  ...
  ```
- **insecure** - Allow insecure SSL connection. Defaults to _false_.
- **logLevel** - Logging level: `BASIC`, `HEADERS`, `VERBOSE`. Defaults to _BASIC_.
- **outputFile** - Program standard and error output will be redirected to the file specified
  by this optional field.
- **privateEnvironmentFile** - Name of the private environment file,
  e.g. `http-client.private.env.json`.
- **privateEnvironmentVariables** - Private environment variables.
- **quietLog** - When combined with `useMavenLogger=true`, prints all executed program
  output at `DEBUG` level instead of the default `INFO` level to the Maven logger.
- **report** - Creates report about execution in JUnit XML Format. Puts it in folder `reports`
  in the current directory. Defaults to _false_.
- **skip** - Skip the execution. Defaults to _false_.
- **socketTimeout** - Number of milliseconds for socket read. Defaults to _10000_.
- **timeout** - Number of milliseconds for execution.
- **useMavenLogger** - When enabled, program standard and error output will be redirected
  to the Maven logger as `INFO` and `ERROR` level logs, respectively.
- **workingDirectory** - The working directory. This is optional: if not specified, the current
  directory will be used.
