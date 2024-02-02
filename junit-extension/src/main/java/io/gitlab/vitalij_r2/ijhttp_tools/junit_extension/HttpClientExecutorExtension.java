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

import static java.util.Objects.nonNull;

import java.time.Duration;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.jetbrains.annotations.VisibleForTesting;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

class HttpClientExecutorExtension implements ParameterResolver {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      HttpClientCommandLineExtension.class);

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
    var executor = getExecutor();
    var duration = getDuration(annotationTimeout);

    if (nonNull(duration)) {
      LOGGER.debug(() -> String.format("Set the watchdog (%s) s", duration.getSeconds()));
      executor.setWatchdog(ExecuteWatchdog.builder().setTimeout(duration).get());
    }

    return executor;
  }

  private static DefaultExecutor getExecutor() {
    return DefaultExecutor.builder().get();
  }

  @VisibleForTesting
  Duration getDuration(int annotationTimeout) {
    Duration duration = null;

    if (0 < annotationTimeout) {
      duration = Duration.ofMillis(annotationTimeout);
    }

    return duration;
  }

}
