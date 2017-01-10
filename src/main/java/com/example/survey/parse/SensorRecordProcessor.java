package com.example.survey.parse;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  public Function<Stream<SensorRecord>, Map<SensorType, List<SensorDailyRecords>>> storeRecords() {
    return records -> {
      Map<SensorType, List<SensorDailyRecords>> sensors = new HashMap<>();
      return repository.addRecords(records, sensors);
    };
  }

  public Function<Map<SensorType, List<SensorDailyRecords>>,
          Map<SensorType, List<SensorDailyRecords>>> validateRecords() {
    return sensors -> {
      String error = sensors.values().stream()
          .flatMap(Collection::stream)
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
