package com.example.survey.parse;

import java.time.LocalTime;

public class SensorRecord {

  private final SensorType type;
  private final LocalTime time;

  public SensorRecord(final SensorType type, final LocalTime time) {
    this.type = type;
    this.time = time;
  }

  public SensorType getType() {
    return type;
  }

  public LocalTime getTime() {
    return time;
  }

}
