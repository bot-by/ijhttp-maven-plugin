package io.gitlab.vitalij_r2.ijhttp_tools.junit_extension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("fast")
@ExtendWith(MockitoExtension.class)
class HttpClientCommandLineExtensionFastTest {

  @Mock
  private HttpClientCommandLineParameters annotation;
  @Mock
  private AnnotatedElement annotatedElement;
  @Mock
  private ExtensionContext extensionContext;
  @Mock
  private Parameter parameter;
  @Mock
  private ParameterContext parameterContext;

  private HttpClientCommandLineExtension resolver;

  @BeforeEach
  void setUp() {
    resolver = new HttpClientCommandLineExtension();
  }

  @DisplayName("Unsupported parameter")
  @ParameterizedTest
  @CsvSource({"java.lang.String,false",
      "io.gitlab.vitalij_r2.ijhttp_tools.command_line.HttpClientCommandLine,true"})
  void unsupportedParameter(Class<?> type, Boolean isCommandLineClass) {
    // given
    when(parameterContext.getParameter()).thenReturn(parameter);
    when(parameter.getType()).thenAnswer((invocationOnMock) -> type);
    if (isCommandLineClass) {
      when(parameterContext.isAnnotated(any())).thenReturn(false);
    }

    // when and then
    assertFalse(resolver.supportsParameter(parameterContext, extensionContext));

    verifyNoInteractions(extensionContext);
    verify(parameter).getType();
    if (isCommandLineClass) {
      verify(parameterContext).isAnnotated(HttpClientCommandLineParameters.class);
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
        (invocationOnMock) -> io.gitlab.vitalij_r2.ijhttp_tools.command_line.HttpClientCommandLine.class);
    when(parameterContext.isAnnotated(HttpClientCommandLineParameters.class)).thenReturn(true);

    // when and then
    assertTrue(resolver.supportsParameter(parameterContext, extensionContext));

    verifyNoInteractions(extensionContext);
    verify(parameter).getType();
    verify(parameterContext).isAnnotated(any());
    verify(parameterContext).getParameter();
  }

}