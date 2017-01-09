package com.example.survey.parse;

import java.util.regex.Pattern;

/**
 * I parse record and convert it into a sensor record with type A.
 */
public class SensorARecordParser extends AbstractRecordParser {

  private final static String PATTERN = "^[A|a]([1-9])([0-9]{1,7})?$";
  private final Pattern pattern = Pattern.compile(PATTERN);

  @Override
  protected Pattern getPattern() {
    return pattern;
  }

  @Override
  protected SensorType getSensorType() {
    return SensorType.A;
  }

}
