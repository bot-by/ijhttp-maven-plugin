/*
 * Copyright 2023 Vitalij Berdinskih
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.bot_by.ijhttp_tools.command_line;

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