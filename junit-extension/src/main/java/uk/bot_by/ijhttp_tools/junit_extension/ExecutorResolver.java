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

import java.time.Duration;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

class ExecutorResolver implements ParameterResolver {

  private final int timeout;

  ExecutorResolver(int timeout) {
    this.timeout = timeout;
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return Executor.class == parameterContext.getParameter().getType()
        && parameterContext.isAnnotated(HttpClientExecutor.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    var annotationTimeout = parameterContext.getAnnotatedElement()
        .getAnnotation(HttpClientExecutor.class).timeout();
    var executor = DefaultExecutor.builder().get();

    if (0 != annotationTimeout) {
      if (0 < annotationTimeout) {
        executor.setWatchdog(getWatchdog(annotationTimeout));
      } else if (0 < timeout) {
        executor.setWatchdog(getWatchdog(timeout));
      }
    }

    return executor;
  }

  @NotNull
  private static ExecuteWatchdog getWatchdog(int annotationTimeout) {
    return ExecuteWatchdog.builder().setTimeout(Duration.ofMillis(annotationTimeout)).get();
  }

}
