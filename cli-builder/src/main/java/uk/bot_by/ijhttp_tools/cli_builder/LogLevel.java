package uk.bot_by.ijhttp_tools.cli_builder;

/**
 * Logging levels.
 */
public enum LogLevel {

  /**
   * Print out HTTP request filename, names and values of public variables, names of private
   * variables, names and URLs of requests.
   */
  BASIC,
  /**
   * Add to BASIC level HTTP headers.
   */
  HEADERS,
  /**
   * Add to HEADERS level request and response bodies, execution statistics.
   */
  VERBOSE

}