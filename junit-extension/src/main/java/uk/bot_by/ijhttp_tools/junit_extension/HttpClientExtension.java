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

import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * Run integration tests using IntelliJ HTTP Client.
 *
 * @author Vitalij Berdinskih
 * @see <a href="https://www.jetbrains.com/help/idea/http-client-cli.html">HTTP Client CLI</a>
 * @since 1.3.0
 */
public class HttpClientExtension implements ParameterResolver {

  private final Set<ParameterResolver> parameterResolvers;

  public HttpClientExtension() {
    parameterResolvers = new LinkedHashSet<>();
    parameterResolvers.add(new ExecutorResolver(-1));
    parameterResolvers.add(new HttpClientCommandLineResolver());
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterResolvers.stream().anyMatch(
        parameterResolver -> parameterResolver.supportsParameter(parameterContext,
            extensionContext));
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    var parameterResolverList = parameterResolvers.stream().filter(
        parameterResolver -> parameterResolver.supportsParameter(parameterContext,
            extensionContext)).toList();

    if (1 < parameterResolverList.size()) {
      throw new ParameterResolutionException(
          String.format("Too many factories: %s for parameter: %s", parameterResolverList,
              parameterContext.getParameter()));
    }

    return parameterResolverList.iterator().next()
        .resolveParameter(parameterContext, extensionContext);
  }

}
