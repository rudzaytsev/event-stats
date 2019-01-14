package org.events;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class EventStatsCounterTest {

  private EventStats stats;
  private EventStatsCounter counter;
  private final LocalDateTime now = LocalDateTime.now();

  @Before
  public void setUp() {
    counter = spy(new EventStatsCounter());
    doReturn(now).when(counter).getCurrentTime();
    stats = counter;
  }

  @Test
  public void getCurrentTimeMethodSpying() {
    assertEquals(now, counter.getCurrentTime());
  }

  @Test
  public void recentlyRegisteredEventShouldBeCounted() {
    LocalDateTime eventTime = now.minusSeconds(1);
    stats.registerEvent(eventTime);
    assertEquals(1L, stats.countEventsOfLastMinute());
  }

  @Test
  public void eventsOfLastMinuteShouldBeCounted() {
    List<LocalDateTime> eventTimes = asList(
      now.minusSeconds(5), now.minusSeconds(59),
      now.minusMinutes(1), now.minusSeconds(60),
      now.minusMinutes(3), now.minusHours(1)
    );
    registerEvents(eventTimes);
    assertEquals(4L, stats.countEventsOfLastMinute());
  }

  @Test
  public void eventsOfLastMinutedShouldBeZeroIfAllEventsAreTooOld() {
    List<LocalDateTime> oldEventTimes = asList(
      now.minusSeconds(61), now.minusMinutes(2),
      now.minusMinutes(5), now.minusHours(3)
    );
    registerEvents(oldEventTimes);
    assertEquals(0L, stats.countEventsOfLastMinute());
  }

  @Test
  public void eventsOfLastHourShouldBeCounted() {
    List<LocalDateTime> eventTimes = asList(
      now.minusSeconds(30), now.minusSeconds(40), now.minusSeconds(55),
      now.minusMinutes(10), now.minusMinutes(25), now.minusMinutes(59),
      now.minusHours(1), now.minusHours(2), now.minusHours(3)
    );
    registerEvents(eventTimes);
    assertEquals(7L, stats.countEventsOfLastHour());
  }

  @Test
  public void eventsOfLastHourShouldBeZeroIfAllEventsAreTooOld() {
    List<LocalDateTime> oldEventTimes = asList(
      now.minusMinutes(90), now.minusMinutes(61), now.minusHours(4), now.minusDays(1)
    );
    registerEvents(oldEventTimes);
    assertEquals(0L, stats.countEventsOfLastHour());
  }

  @Test
  public void eventsOfLastDayShouldBeCounted() {
    List<LocalDateTime> eventTimes = asList(
      now.minusHours(5), now.minusHours(5), now.minusHours(12),
      now.minusHours(24), now.minusDays(1), now.minusDays(2), now.minusDays(3)
    );
    registerEvents(eventTimes);
    assertEquals(3L, stats.countEventsOfLastDay());
  }

  @Test
  public void eventsOfLastDayShouldBeZeroIfAllEventsAreTooOld() {
    List<LocalDateTime> oldEventTimes = asList(
      now.minusHours(24), now.minusHours(36), now.minusHours(90)
    );
    registerEvents(oldEventTimes);
    assertEquals(0L, stats.countEventsOfLastDay());
  }

  private void registerEvents(List<LocalDateTime> eventTimes) {
    for (LocalDateTime eventTime : eventTimes) {
      stats.registerEvent(eventTime);
    }
  }
}