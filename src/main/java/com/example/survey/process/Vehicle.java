package com.example.survey.process;

import com.example.survey.parse.SensorType;

import java.time.LocalTime;
import java.util.Objects;

public class Vehicle {

  private final SensorType sensorType;
  private final int day;
  private final LocalTime axle1Time;
  private final LocalTime axle2Time;

  public Vehicle(final SensorType sensorType, final int day, final LocalTime axle1Time, final LocalTime axle2Time) {
    this.sensorType = sensorType;
    this.day = day;
    this.axle1Time = axle1Time;
    this.axle2Time = axle2Time;
  }

  public SensorType getSensorType() {
    return sensorType;
  }

  public int getDay() {
    return day;
  }

  public LocalTime getAxle1Time() {
    return axle1Time;
  }

  public LocalTime getAxle2Time() {
    return axle2Time;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }

    if (o instanceof Vehicle) {
      Vehicle other = (Vehicle) o;
      return Objects.equals(sensorType, other.sensorType)
          && Objects.equals(day, other.day)
          && Objects.equals(axle1Time, other.axle1Time)
          && Objects.equals(axle2Time, other.axle2Time);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(sensorType, day, axle1Time, axle2Time);
  }

}
