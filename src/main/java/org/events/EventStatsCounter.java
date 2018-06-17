package org.events;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Counter of registered events
 */
public class EventStatsCounter implements EventStats {

  private ConcurrentLinkedQueue<LocalDateTime> eventTimes = new ConcurrentLinkedQueue<LocalDateTime>();

  @Override
  public void registerEvent(LocalDateTime dateTime) {
    eventTimes.offer(dateTime);
  }

  @Override
  public long countEventsOfLastMinute() {
    LocalDateTime currentTime = getCurrentTime();
    Iterator<LocalDateTime> iter = eventTimes.iterator();
    long sum = 0;
    while(iter.hasNext()) {
      LocalDateTime eventTime = iter.next();
      if (eventTime.isBefore(currentTime) && eventTime.isAfter(currentTime.minusMinutes(1))) {
        sum++;
      }
    }
    return sum;
  }

  LocalDateTime getCurrentTime() {
    return LocalDateTime.now();
  }

  @Override
  public long countEventsOfLastHour() {
    return 0;
  }

  @Override
  public long countEventsOfLastDay() {
    return 0;
  }
}
