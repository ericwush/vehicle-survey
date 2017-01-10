package com.example.survey.sensor;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Pattern;

public abstract class AbstractRecordParser implements RecordParser {

  @Override
  public Optional<SensorRecord> parse(final String input) {
    return Optional.ofNullable(input)
        .filter(in -> getPattern().matcher(in).matches())
        .map(in -> in.substring(1))
        .map(Integer::new)
        .filter(millis -> millis < 24 * 60 * 60 * 1000)
        .map(millis -> LocalTime.of(0, 0).plus(millis, ChronoUnit.MILLIS))
        .map(time -> new SensorRecord(getSensorType(), time));
  }

  protected abstract Pattern getPattern();

  protected abstract SensorType getSensorType();

}
