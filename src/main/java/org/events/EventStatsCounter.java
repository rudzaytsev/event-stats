package org.events;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.time.temporal.ChronoUnit.*;

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
    return countEventsInLastOneOf(MINUTES);
  }

  private long countEventsInLastOneOf(ChronoUnit timeUnit) {
    LocalDateTime now = getCurrentTime();
    Iterator<LocalDateTime> iter = eventTimes.iterator();
    long sum = 0;
    while(iter.hasNext()) {
      LocalDateTime eventTime = iter.next();
      if (eventTime.isBefore(now) && eventTime.isAfter(now.minus(1, timeUnit))) {
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
    return countEventsInLastOneOf(HOURS);
  }

  @Override
  public long countEventsOfLastDay() {
    return countEventsInLastOneOf(DAYS);
  }
}
