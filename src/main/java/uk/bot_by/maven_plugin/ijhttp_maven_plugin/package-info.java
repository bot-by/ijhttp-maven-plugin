/**
 * The plugin allows to run HTTP requests on the <em>integration-test</em> phase.
 * <p>
 * <strong>Important!</strong> The plugin does not contain the HTTP client: you need to install it
 * by yourself then add to {@code PATH}. You can also set the full path to the ijhttp via the
 * parameter
 * {@linkplain uk.bot_by.maven_plugin.ijhttp_maven_plugin.RunMojo#setExecutable(java.lang.String)
 * executable}.
 *
 * @author Witalij Berdinskich
 * @since 1.0.0
 */
package uk.bot_by.maven_plugin.ijhttp_maven_plugin;
