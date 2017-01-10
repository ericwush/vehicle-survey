package com.example.survey.sensor;

import java.time.LocalTime;
import java.util.Objects;

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

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }

    if (o instanceof SensorRecord) {
      SensorRecord other = (SensorRecord) o;
      return Objects.equals(type, other.type) && Objects.equals(time, other.time);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, time);
  }

}
