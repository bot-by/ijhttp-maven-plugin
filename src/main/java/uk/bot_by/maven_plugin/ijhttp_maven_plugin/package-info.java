/**
 * The plugin allows to run HTTP requests on the <em>integration-test</em> phase.
 * <p>
 * <strong>Important!</strong> The plugin does not contain the HTTP client: you need to install it
 * by yourself then add to {@code PATH}. You can also set the full path to the ijhttp via the
 * parameter
 * {@linkplain uk.bot_by.maven_plugin.ijhttp_maven_plugin.RunMojo#setExecutable(java.lang.String)
 * executable}. The <a href="https://gitlab.com/vitalijr2/ijhttp-demo">IntelliJ HTTP Client Demo</a>
 * has some examples how to download the HTTP client.
 *
 * @author Vitalij Berdinskih
 * @since 1.0.0
 */
package uk.bot_by.maven_plugin.ijhttp_maven_plugin;
