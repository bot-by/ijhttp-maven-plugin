package uk.bot_by.ijhttp_tools.junit_extension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("fast")
@ExtendWith(MockitoExtension.class)
class HttpClientExtensionTest {

  @Mock
  private ExtensionContext extensionContext;
  @Mock
  private Parameter parameter;
  @Mock
  private ParameterContext parameterContext;
  @Mock
  private ParameterResolver resolverA;
  @Mock
  private ParameterResolver resolverB;

  private HttpClientExtension extension;

  @BeforeEach
  void setUp() {
    extension = new HttpClientExtension(resolverA, resolverB);
  }

  @DisplayName("Is parameter supported?")
  @ParameterizedTest
  @CsvSource({"false,false,false", "false,true,true", "true,false,true", "true,true,true"})
  void supportsParameter(boolean isSupportedByA, boolean isSupportedByB, boolean isSupported) {
    // given
    when(resolverA.supportsParameter(any(), any())).thenReturn(isSupportedByA);
    if (!(isSupportedByA && isSupported)) {
      when(resolverB.supportsParameter(any(), any())).thenReturn(isSupportedByB);
    }

    // when and then
    assertEquals(isSupported, extension.supportsParameter(parameterContext, extensionContext));

    verify(resolverA).supportsParameter(eq(parameterContext), eq(extensionContext));
    if (!(isSupportedByA && isSupported)) {
      verify(resolverB).supportsParameter(eq(parameterContext), eq(extensionContext));
    }
  }

  @DisplayName("First resolver resolves a parameter")
  @Test
  void firstResolverResolvesParameter() {
    // given
    when(resolverA.resolveParameter(any(), any())).thenReturn(this);
    when(resolverA.supportsParameter(any(), any())).thenReturn(true);
    when(resolverB.supportsParameter(any(), any())).thenReturn(false);

    // when and then
    assertNotNull(extension.resolveParameter(parameterContext, extensionContext));
  }

  @DisplayName("Next resolver resolves a parameter")
  @Test
  void nextResolverResolvesParameter() {
    // given
    when(resolverA.supportsParameter(any(), any())).thenReturn(false);
    when(resolverB.resolveParameter(any(), any())).thenReturn(this);
    when(resolverB.supportsParameter(any(), any())).thenReturn(true);

    // when and then
    assertNotNull(extension.resolveParameter(parameterContext, extensionContext));
  }

  @DisplayName("Two resolvers resolve a parameter")
  @Test
  void twoResolversResolveParameter() {
    // given
    when(resolverA.supportsParameter(any(), any())).thenReturn(true);
    when(resolverB.supportsParameter(any(), any())).thenReturn(true);
    when(parameter.toString()).thenReturn("test parameter");
    when(parameterContext.getParameter()).thenReturn(parameter);

    // when
    var exception = assertThrows(ParameterResolutionException.class,
        () -> extension.resolveParameter(parameterContext, extensionContext));

    // then
    assertEquals("Too many factories: [resolverA, resolverB] for parameter: test parameter",
        exception.getMessage());
  }

}