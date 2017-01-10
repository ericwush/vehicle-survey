package com.example.survey.parse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * I read records file and parse records line by line.
 */
public class SensorRecordFileParser {

  private final SensorRecordProcessor processor;

  public SensorRecordFileParser(final SensorRecordProcessor processor) {
    this.processor = processor;
  }

  public Map<SensorType, List<SensorDailyRecords>> parse(final String filename) {
    try (Stream<String> lines = Files.lines(Paths.get(filename))) {
      return processor.parseRecords()
          .andThen(processor.storeRecords())
          .andThen(processor.validateRecords())
          .apply(lines);
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not parse file " + filename);
    }
  }

}
