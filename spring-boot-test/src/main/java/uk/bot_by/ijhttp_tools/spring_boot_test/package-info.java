/**
 * Spring Boot Test autoconfiguration allows to run HTTP requests using the IntelliJ HTTP Client.
 *
 * <dl>
 *   <dt>{@link uk.bot_by.ijhttp_tools.spring_boot_test.AutoConfigureHttpClientCommandLine}</dt>
 *   <dd>Spring Boot Test autoconfiguration annotation.</dd>
 *   <dt>{@link uk.bot_by.ijhttp_tools.spring_boot_test.HttpClientCommandLineConfiguration}</dt>
 *   <dd>HTTP Client configuration provides
 *   {@linkplain org.apache.commons.exec.Executor executor} and
 *   {@linkplain uk.bot_by.ijhttp_tools.command_line.HttpClientCommandLine command line} beans.</dd>
 *   <dt>{@link uk.bot_by.ijhttp_tools.spring_boot_test.HttpClientCommandLineParameters}</dt>
 *   <dd>HTTP Client parameters from Spring Boot properties.</dd>
 * </dl>
 *
 * @author Vitalij Berdinskih
 * @since 1.1.0
 */
package uk.bot_by.ijhttp_tools.spring_boot_test;