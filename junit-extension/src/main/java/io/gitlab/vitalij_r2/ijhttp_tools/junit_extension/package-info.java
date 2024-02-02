/**
 * The jUnit Extension allows to run HTTP requests on the <em>integration-test</em> phase using the
 * IntelliJ HTTP Client.
 * <p>
 * The <a href="https://github.com/JetBrains/http-request-in-editor-spec">HTTP Request in Editor
 * Specification</a> describes format these files.
 * <p>
 * <strong>Important!</strong> The plugin does not contain the HTTP client: you need to install it
 * by yourself then add to {@code PATH}. You can also set the full path to the ijhttp via the
 * parameter
 * {@linkplain io.gitlab.vitalij_r2.ijhttp_tools.junit_extension.HttpClientCommandLineParameters#executable()
 * executable}.
 * <p>
 * The <a href="https://gitlab.com/vitalijr2/ijhttp-demo">IntelliJ HTTP Client Demo</a> has some
 * examples how to download the HTTP client.
 *
 * @author Vitalij Berdinskih
 * @see <a href="https://www.youtube.com/live/mwiHAukbWjM?feature=share">Live stream: The New HTTP
 * Client CLI</a>
 * @since 1.3.0
 */
package io.gitlab.vitalij_r2.ijhttp_tools.junit_extension;