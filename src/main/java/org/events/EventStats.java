package org.events;

import java.time.LocalDateTime;

/**
 * Interface for registration and counting events
 *
 */
public interface EventStats {

  /**
   * Register an event with concrete date and time
   *
   * @param dateTime the date and time of event to register
   */
  void registerEvent(LocalDateTime dateTime);

  /**
   * Count number of events which were registered in period of last minute
   *
   * @return number of events which were registered in period of last minute
   */
  long countEventsOfLastMinute();

  /**
   * Count number of events which were registered in period of last hour
   *
   * @return number of events which were registered in period of last hour
   */
  long countEventsOfLastHour();

  /**
   * Count number of events which were registered in period of last 24 hours
   *
   * @return  number of events which were registered in period of last 24 hours
   */
  long countEventsOfLastDay();
}
