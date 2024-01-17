package uk.bot_by.ijhttp_tools.junit_extension;

import org.apache.commons.exec.Executor;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;

public class ExecutorResolver extends TypeBasedParameterResolver<Executor>, BeforeAllCallback {

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {

  }

  @Override
  public Executor resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    parameterContext.getParameter().getAnnotation()
    return null;
  }
}
