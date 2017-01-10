package com.example.survey.parse;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * I parse sensor records and store in repository accordingly.
 */
public class SensorRecordProcessor {

  private final RecordParser parser;
  private final SensorRecordRepository repository;

  public SensorRecordProcessor(final RecordParser parser, final SensorRecordRepository repository) {
    this.parser = parser;
    this.repository = repository;
  }

  public Function<Stream<String>, Stream<SensorRecord>> parseRecords() {
    return lines ->
      lines.map(String::trim)
          .map(parser::parse)
          .filter(Optional::isPresent)
          .map(Optional::get);
  }

  public Function<Stream<SensorRecord>, List<SensorDailyRecords>> storeRecords() {
    return repository::addRecords;
  }

  public Function<List<SensorDailyRecords>, List<SensorDailyRecords>> validateRecords() {
    return sensors -> {
      String error = sensors.stream()
          .map(SensorDailyRecords::validate)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .map(Throwable::getMessage)
          .collect(joining("\n"));
      if (error.length() > 0) {
        throw new IllegalStateException(error);
      }
      return sensors;
    };
  }

}
