# event-stats
Library which collects statistics about events

## How to build
Library use Maven as build system

In order to build library run command

```
$ mvn install
```

After this you can use library in your projects.
Just add dependency into you pom.xml or it's analog if you use another build system

```
<dependency>
    <groupId>org.events</groupId>
    <artifactId>event-stats</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```
## How to run tests
In order to test library run command

```
$ mvn test
```

## How to use

Library classes can be used this way

```
EventStats stats = new EventStatsCounter();

// in first thread
stats.registerEvent(LocalDateTime.now());

//...

// in second thread
// waiting for events to come
Thread.sleep(1000);
stats.countEventsOfLastMinute();

```
