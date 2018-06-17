package org.events;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

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
    assertEquals(1, stats.countEventsOfLastMinute());
  }
}