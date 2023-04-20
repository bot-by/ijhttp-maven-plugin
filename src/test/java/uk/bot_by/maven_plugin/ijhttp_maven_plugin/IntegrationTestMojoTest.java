package uk.bot_by.maven_plugin.ijhttp_maven_plugin;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("fast")
class IntegrationTestMojoTest {

  @Mock
  private Log logger;
  @InjectMocks
  private IntegrationTestMojo mojo;

  @Test
  void test() throws MojoExecutionException {
    // when
    mojo.execute();

    // then
    verify(logger).info(anyString());
  }

}