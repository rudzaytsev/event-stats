package org.events;

import org.junit.After;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.time.LocalDateTime.now;
import static org.junit.Assert.assertEquals;

/**
 * Represents experiments with EventStats in parallel
 */
public class ParallelEventStatsTest {

  private EventStats stats = new EventStatsCounter();
  private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors() + 5;
  private ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

  @Test
  public void countingEventsAfterWorkingInParallelUnderLoad() throws Exception {
    Runnable event = new Runnable() {
      @Override
      public void run() {
        stats.registerEvent(now());
      }
    };

    final int EVENTS = 10000;
    for (int i = 0; i < EVENTS; i++) {
      executor.submit(event);
    }

    final int ONE_SECOND = 1000;
    Thread.sleep(ONE_SECOND);

    assertEquals(EVENTS, stats.countEventsOfLastMinute());
    assertEquals(EVENTS, stats.countEventsOfLastHour());
    assertEquals(EVENTS, stats.countEventsOfLastDay());
  }

  @After
  public void tearDown() throws Exception {
    executor.shutdown();
  }
}
