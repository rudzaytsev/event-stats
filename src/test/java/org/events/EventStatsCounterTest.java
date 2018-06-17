package org.events;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class EventStatsCounterTest {

  private EventStats stats;
  private EventStatsCounter counter;
  private final LocalDateTime now = LocalDateTime.now();

  @Before
  public void setUp() throws Exception {
    counter = spy(new EventStatsCounter());
    doReturn(now).when(counter).getCurrentTime();
    stats = counter;
  }

  @Test
  public void getCurrentTimeMethodSpying() throws Exception {
    assertEquals(now, counter.getCurrentTime());
  }

  @Test
  public void recentlyRegisteredEventShouldBeCounted() {
    LocalDateTime eventTime = now.minusSeconds(1);
    stats.registerEvent(eventTime);
    assertEquals(1L, stats.countEventsOfLastMinute());
  }

  @Test
  public void eventsOfLastMinuteShouldBeCounted() throws Exception {
    List<LocalDateTime> eventTimes = Arrays.asList(
      now.minusSeconds(5), now.minusSeconds(59),
      now.minusMinutes(1), now.minusMinutes(3),
      now.minusHours(1)
    );
    registerEvents(eventTimes);
    assertEquals(2L, stats.countEventsOfLastMinute());
  }

  @Test
  public void eventsOfLastMinutedShouldBeZeroIfAllEventsAreTooOld() throws Exception {
    List<LocalDateTime> oldEventTimes = Arrays.asList(
      now.minusSeconds(60), now.minusMinutes(2),
      now.minusMinutes(5),  now.minusHours(3)
    );
    registerEvents(oldEventTimes);
    assertEquals(0L, stats.countEventsOfLastMinute());
  }


  private void registerEvents(List<LocalDateTime> eventTimes) {
    for (LocalDateTime eventTime : eventTimes) {
      stats.registerEvent(eventTime);
    }
  }
}