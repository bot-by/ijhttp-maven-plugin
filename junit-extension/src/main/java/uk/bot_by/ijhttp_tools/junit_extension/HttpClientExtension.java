/*
 * Copyright 2023-2024 bot-by
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
package uk.bot_by.ijhttp_tools.junit_extension;

import java.util.stream.Stream;
import org.jetbrains.annotations.VisibleForTesting;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

/**
 * Run integration tests using IntelliJ HTTP Client.
 *
 * @author Vitalij Berdinskih
 * @see <a href="https://www.jetbrains.com/help/idea/http-client-cli.html">HTTP Client CLI</a>
 * @since 1.3.0
 */
public class HttpClientExtension implements ParameterResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientExtension.class);

  private final ParameterResolver[] parameterResolvers;

  public HttpClientExtension() {
    this(new HttpClientExecutorResolver(), new HttpClientCommandLineResolver());
  }

  @VisibleForTesting
  HttpClientExtension(ParameterResolver... parameterResolvers) {
    this.parameterResolvers = parameterResolvers;
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return Stream.of(parameterResolvers).anyMatch(
        parameterResolver -> parameterResolver.supportsParameter(parameterContext,
            extensionContext));
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    var parameterResolverList = Stream.of(parameterResolvers).filter(
        parameterResolver -> parameterResolver.supportsParameter(parameterContext,
            extensionContext)).toList();

    LOGGER.debug(() -> String.format("Parameter resolvers %s", parameterResolverList));
    if (1 < parameterResolverList.size()) {
      throw new ParameterResolutionException(
          String.format("Too many factories: %s for parameter: %s", parameterResolverList,
              parameterContext.getParameter()));
    }

    return parameterResolverList.iterator().next()
        .resolveParameter(parameterContext, extensionContext);
  }

}
