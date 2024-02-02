/**
 * Spring Boot Test autoconfiguration allows to run HTTP requests using the IntelliJ HTTP Client.
 * <p>
 * The <a href="https://github.com/JetBrains/http-request-in-editor-spec">HTTP Request in Editor
 * Specification</a> describes format these files.
 * <p>
 * <strong>Important!</strong> The plugin does not contain the HTTP client: you need to install it
 * by yourself then add to {@code PATH}. You can also set the full path to the ijhttp via the
 * parameter
 * {@linkplain
 * io.gitlab.vitalij_r2.ijhttp_tools.spring_boot_test.HttpClientCommandLineParameters#setExecutable(java.lang.String)}
 * executable}.
 * <dl>
 *   <dt>{@link io.gitlab.vitalij_r2.ijhttp_tools.spring_boot_test.AutoConfigureHttpClientCommandLine}</dt>
 *   <dd>Spring Boot Test autoconfiguration annotation.</dd>
 *   <dt>{@link io.gitlab.vitalij_r2.ijhttp_tools.spring_boot_test.HttpClientCommandLineConfiguration}</dt>
 *   <dd>HTTP Client configuration provides
 *   {@linkplain org.apache.commons.exec.Executor executor} and
 *   {@linkplain io.gitlab.vitalij_r2.ijhttp_tools.command_line.HttpClientCommandLine command line} beans.</dd>
 *   <dt>{@link io.gitlab.vitalij_r2.ijhttp_tools.spring_boot_test.HttpClientCommandLineParameters}</dt>
 *   <dd>HTTP Client parameters from Spring Boot properties.</dd>
 * </dl>
 * <p>
 * The <a href="https://gitlab.com/vitalijr2/ijhttp-demo">IntelliJ HTTP Client Demo</a> has some
 * examples how to download the HTTP client.
 *
 * @author Vitalij Berdinskih
 * @see <a href="https://www.youtube.com/live/mwiHAukbWjM?feature=share">Live stream: The New HTTP
 * Client CLI</a>
 * @since 1.1.0
 */
package io.gitlab.vitalij_r2.ijhttp_tools.spring_boot_test;