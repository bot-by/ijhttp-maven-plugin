package io.gitlab.vitalij_r2.ijhttp_tools.junit_extension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.gitlab.vitalij_r2.ijhttp_tools.command_line.HttpClientCommandLine;

@Tag("slow")
@ExtendWith(MockitoExtension.class)
class HttpClientCommandLineExtensionSlowTest {

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

  @DisplayName("Resolve parameter")
  @Test
  void resolveParameter() {
    // given
    when(annotatedElement.getAnnotation(any())).thenReturn(annotation);
    when(parameterContext.getAnnotatedElement()).thenReturn(annotatedElement);
    // default values
    when(annotation.connectTimeout()).thenReturn(-1);
    when(annotation.directories()).thenReturn(new String[0]);
    when(annotation.dockerMode()).thenReturn(false);
    when(annotation.environmentFile()).thenReturn("");
    when(annotation.environmentName()).thenReturn("");
    when(annotation.environmentVariables()).thenReturn(new String[0]);
    when(annotation.executable()).thenReturn("ijhttp");
    when(annotation.insecure()).thenReturn(false);
    when(annotation.logLevel()).thenReturn("BASIC");
    when(annotation.privateEnvironmentFile()).thenReturn("");
    when(annotation.privateEnvironmentVariables()).thenReturn(new String[0]);
    when(annotation.proxy()).thenReturn("");
    when(annotation.report()).thenReturn(false);
    when(annotation.reportPath()).thenReturn("");
    when(annotation.socketTimeout()).thenReturn(-1);
    // test values
    when(annotation.files()).thenReturn(new String[]{"*"});

    var spiedResolver = spy(resolver);

    // when and then
    var commandLine = (HttpClientCommandLine) spiedResolver.resolveParameter(parameterContext,
        extensionContext);

    verifyNoInteractions(extensionContext);
    verify(annotatedElement).getAnnotation(HttpClientCommandLineParameters.class);
    verify(parameterContext).getAnnotatedElement();

    assertAll("Default command line", () -> assertNotNull(commandLine),
        () -> assertEquals("ijhttp", commandLine.getCommandLine().getExecutable()),
        () -> assertThat(commandLine.getCommandLine().getArguments(), arrayContaining("*")));
  }

  @DisplayName("Resolve parameter with custom configuration")
  @Test
  void customConfiguration() {
    // given
    when(annotatedElement.getAnnotation(any())).thenReturn(annotation);
    when(parameterContext.getAnnotatedElement()).thenReturn(annotatedElement);
    // default values
    when(annotation.files()).thenReturn(new String[0]);
    // custom values
    when(annotation.connectTimeout()).thenReturn(123);
    when(annotation.directories()).thenReturn(new String[]{"src/test/resources"});
    when(annotation.dockerMode()).thenReturn(true);
    when(annotation.environmentFile()).thenReturn("env.file");
    when(annotation.environmentName()).thenReturn("test");
    when(annotation.environmentVariables()).thenReturn(new String[]{"name=value"});
    when(annotation.executable()).thenReturn("ijhttp.exe");
    when(annotation.insecure()).thenReturn(true);
    when(annotation.logLevel()).thenReturn("HEADERS");
    when(annotation.privateEnvironmentFile()).thenReturn("private.file");
    when(annotation.privateEnvironmentVariables()).thenReturn(new String[]{"private=hidden"});
    when(annotation.proxy()).thenReturn("http://127.0.0.1:12345");
    when(annotation.report()).thenReturn(true);
    when(annotation.reportPath()).thenReturn("report.dir");
    when(annotation.socketTimeout()).thenReturn(987);

    var spiedResolver = spy(resolver);

    // when and then
    var commandLine = (HttpClientCommandLine) spiedResolver.resolveParameter(parameterContext,
        extensionContext);

    verifyNoInteractions(extensionContext);
    verify(annotatedElement).getAnnotation(HttpClientCommandLineParameters.class);
    verify(parameterContext).getAnnotatedElement();

    assertAll("Custom command line", () -> assertNotNull(commandLine),
        () -> assertEquals("ijhttp.exe", commandLine.getCommandLine().getExecutable()),
        () -> assertThat(commandLine.getCommandLine().getArguments(),
            arrayContaining(equalTo("--docker-mode"), equalTo("--insecure"), equalTo("--log-level"),
                equalTo("HEADERS"), equalTo("--connect-timeout"), equalTo("123"),
                equalTo("--socket-timeout"), equalTo("987"), equalTo("--env"), equalTo("test"),
                equalTo("--env-file"), endsWith("env.file"), equalTo("--env-variables"),
                equalTo("name=value"), equalTo("--private-env-file"), endsWith("private.file"),
                equalTo("--private-env-variables"), equalTo("private=hidden"), equalTo("--proxy"),
                equalTo("http://127.0.0.1:12345"), endsWith("test.http"), equalTo("--report"),
                endsWith("report.dir"))));
  }

}