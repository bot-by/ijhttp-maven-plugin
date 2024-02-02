/*
 * Copyright 2023-2024 Vitalij Berdinskih
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gitlab.vitalij_r2.ijhttp_tools.junit_extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * HTTP Client parameters.
 *
 * @author Vitalij Berdinskih
 * @see <a href="https://www.jetbrains.com/help/idea/http-client-cli.html">HTTP Client CLI</a>
 * @since 1.3.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(HttpClientCommandLineExtension.class)
public @interface HttpClientCommandLineParameters {

  /**
   * Number of milliseconds for connection. Defaults to <em>3000</em>.
   */
  int connectTimeout() default -1;

  /**
   * Directories to look up HTTP files. At least one {@code file} or {@code directory} is required.
   */
  String[] directories() default {};

  /**
   * Enables Docker mode. Treat <em>localhost</em> as <em>host.docker.internal</em>. Defaults to
   * <em>false</em>.
   */
  boolean dockerMode() default false;

  /**
   * Name of the public environment file, e.g. <em>http-client.env.json</em>.
   */
  String environmentFile() default "";

  /**
   * Public environment variables.
   */
  String[] environmentVariables() default {};

  /**
   * Name of the environment in a configuration file.
   */
  String environmentName() default "";

  /**
   * The executable. Can be a full path or the name of the executable. Defaults to
   * <em>ijhttp</em>.
   */
  String executable() default "ijhttp";

  /**
   * HTTP file paths. At least one {@code file} or {@code directory} is required.
   */
  String[] files() default {};

  /**
   * Allow insecure SSL connection. Defaults to <em>false</em>.
   */
  boolean insecure() default false;

  /**
   * Logging level: BASIC, HEADERS, VERBOSE. Defaults to <em>BASIC</em>.
   */
  String logLevel() default "BASIC";

  /**
   * Name of the private environment file, e.g. <em>http-client.private.env.json</em>.
   */
  String privateEnvironmentFile() default "";

  /**
   * Private environment variables.
   */
  String[] privateEnvironmentVariables() default {};

  /**
   * Proxy URI.
   * <p>
   * Proxy setting in format <em>scheme://login:password@host:port</em>, <em>scheme<em> can be
   * <em>socks<em> for <strong>SOCKS</strong> or <em>http<em> for <strong>HTTP</strong>.
   */
  String proxy() default "";

  /**
   * Creates report about execution in JUnit XML Format. Defaults to <em>false</em>.
   */
  boolean report() default false;

  /**
   * Path to a report folder. Default value <em>reports</em> in the current directory.
   */
  String reportPath() default "";

  /**
   * Number of milliseconds for socket read. Defaults to <em>10000</em>.
   */
  int socketTimeout() default -1;

}
