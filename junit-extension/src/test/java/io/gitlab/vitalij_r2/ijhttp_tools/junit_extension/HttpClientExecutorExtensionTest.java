package io.gitlab.vitalij_r2.ijhttp_tools.junit_extension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("fast")
@ExtendWith(MockitoExtension.class)
class HttpClientExecutorExtensionTest {

  @Mock
  private HttpClientExecutor annotation;
  @Mock
  private AnnotatedElement annotatedElement;
  @Mock
  private ExtensionContext extensionContext;
  @Mock
  private Parameter parameter;
  @Mock
  private ParameterContext parameterContext;

  private HttpClientExecutorExtension resolver;

  @BeforeEach
  void setUp() {
    resolver = new HttpClientExecutorExtension();
  }

  @DisplayName("Unsupported parameter")
  @ParameterizedTest
  @CsvSource({"java.lang.String,false", "org.apache.commons.exec.Executor,true"})
  void unsupportedParameter(Class<?> type, Boolean isExecutorClass) {
    // given
    when(parameterContext.getParameter()).thenReturn(parameter);
    when(parameter.getType()).thenAnswer((invocationOnMock) -> type);
    if (isExecutorClass) {
      when(parameterContext.isAnnotated(any())).thenReturn(false);
    }

    // when and then
    assertFalse(resolver.supportsParameter(parameterContext, extensionContext));

    verifyNoInteractions(extensionContext);
    verify(parameter).getType();
    if (isExecutorClass) {
      verify(parameterContext).isAnnotated(HttpClientExecutor.class);
    } else {
      verify(parameterContext, never()).isAnnotated(any());
    }
    verify(parameterContext).getParameter();
  }

  @DisplayName("Supported parameter")
  @Test
  void supportsParameter() {
    // given
    when(parameterContext.getParameter()).thenReturn(parameter);
    when(parameter.getType()).thenAnswer(
        (invocationOnMock) -> org.apache.commons.exec.Executor.class);
    when(parameterContext.isAnnotated(HttpClientExecutor.class)).thenReturn(true);

    // when and then
    assertTrue(resolver.supportsParameter(parameterContext, extensionContext));

    verifyNoInteractions(extensionContext);
    verify(parameter).getType();
    verify(parameterContext).isAnnotated(any());
    verify(parameterContext).getParameter();
  }

  @DisplayName("Resolve parameter")
  @ParameterizedTest
  @ValueSource(ints = {-1, 10000})
  void resolveParameter(int timeout) {
    // given
    when(annotation.timeout()).thenReturn(timeout);
    when(annotatedElement.getAnnotation(any())).thenReturn(annotation);
    when(parameterContext.getAnnotatedElement()).thenReturn(annotatedElement);

    var spiedResolver = spy(resolver);

    // when and then
    assertNotNull(spiedResolver.resolveParameter(parameterContext, extensionContext));

    verifyNoInteractions(extensionContext);
    verify(annotation).timeout();
    verify(annotatedElement).getAnnotation(HttpClientExecutor.class);
    verify(parameterContext).getAnnotatedElement();
    verify(spiedResolver).getDuration(timeout);
  }

  @DisplayName("Default timeout")
  @Test
  void defaultTimeout() {
    // when and then
    assertNull(resolver.getDuration(-1));
  }

}