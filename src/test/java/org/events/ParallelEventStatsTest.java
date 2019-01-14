package org.events;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;
import static org.junit.Assert.assertEquals;

/**
 * Represents experiments with EventStats in parallel
 */
public class ParallelEventStatsTest {

  private static final int EVENTS = 10000;
  private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors() + 5;

  private EventStats stats = new EventStatsCounter();
  private ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

  @Ignore("Test may be wrong ")
  @Test
  public void countingEventsAfterWorkingInParallelUnderLoad() throws Exception {
    for (int i = 0; i < EVENTS; i++) {
      executor.submit(() -> stats.registerEvent(now()));
    }

    TimeUnit.SECONDS.sleep(1);

    assertEquals(EVENTS, stats.countEventsOfLastMinute());
    assertEquals(EVENTS, stats.countEventsOfLastHour());
    assertEquals(EVENTS, stats.countEventsOfLastDay());
  }

  @After
  public void tearDown() {
    executor.shutdown();
  }
}
