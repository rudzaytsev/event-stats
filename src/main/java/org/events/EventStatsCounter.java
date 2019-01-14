package org.events;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.LongAdder;

/**
 * Counter of registered events
 */
public class EventStatsCounter implements EventStats {

  public static final int MAX_SECONDS_IN_DAY = 24 * 3600;

  /**
   * number of events in n-th second
   */
  private AtomicLongArray events = new AtomicLongArray(MAX_SECONDS_IN_DAY);

  private ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

  public EventStatsCounter() {
    service.scheduleAtFixedRate(() -> {
      int currentSecond = toSecondOfDay(getCurrentTime());
      int nextSecond = (currentSecond + 1) % MAX_SECONDS_IN_DAY;
      events.getAndSet(nextSecond, 0);
    },
     0, 1, TimeUnit.SECONDS);
  }

  @Override
  public void registerEvent(LocalDateTime dateTime) {
    int secondInDay = toSecondOfDay(dateTime);
    events.incrementAndGet(secondInDay);
  }

  private int toSecondOfDay(LocalDateTime dateTime) {
    return dateTime.getSecond() + dateTime.getMinute() * 60 + dateTime.getHour() * 3600;
  }

  @Override
  public long countEventsOfLastMinute() {
    return countEventsSomeTimeAgo(1, ChronoUnit.MINUTES);
  }

  private long countEventsSomeTimeAgo(long timeAmount, TemporalUnit temporalUnit) {
    LocalDateTime now = getCurrentTime();
    LocalDateTime someTimeAgo = now.minus(timeAmount, temporalUnit);

    int start = toSecondOfDay(someTimeAgo);
    int end = toSecondOfDay(now);

    if (end < start) return 0;

    LongAdder adder = new LongAdder();
    for (int i = start; i < end; i++) {
      adder.add(events.get(i));
    }
    return adder.sum();
  }


  LocalDateTime getCurrentTime() {
    return LocalDateTime.now();
  }

  @Override
  public long countEventsOfLastHour() {
    return countEventsSomeTimeAgo(1, ChronoUnit.HOURS);
  }

  @Override
  public long countEventsOfLastDay() {
    return countEventsSomeTimeAgo(1, ChronoUnit.DAYS);
  }
}
