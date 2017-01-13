package com.example.survey.sensor;

import java.util.Optional;

/**
 * I have a chain of parsers to parse sensor records.
 */
public class SensorRecordParser implements RecordParser {

  private final RecordParser[] parsers;

  public SensorRecordParser(final RecordParser... parsers) {
    this.parsers = parsers;
  }

  @Override
  public Optional<SensorRecord> parse(final String input) {
    for (final RecordParser parser : parsers) {
      final Optional<SensorRecord> maybeSensorRecord = parser.parse(input);
      if (maybeSensorRecord.isPresent()) {
        return maybeSensorRecord;
      }
    }
    return Optional.empty();
  }

}
