package com.example.survey.vehicle;

import com.example.survey.sensor.SensorType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.OptionalInt;

public class Vehicle {

  // This can be externalized to configuration
  private static final BigDecimal KMS_BETWEEN_AXLES = new BigDecimal(2.5 / 1000);
  private static final BigDecimal HOUR_IN_MILLIS = new BigDecimal(60 * 60 * 1000);
  private static final BigDecimal KM_IN_METERS = new BigDecimal(1000);

  private final SensorType sensorType;
  private final int day;
  private final LocalTime axle1Time;
  private final LocalTime axle2Time;
  private final int speed;
  private final OptionalInt distance;

  public Vehicle(final SensorType sensorType,
                 final int day,
                 final LocalTime axle1Time,
                 final LocalTime axle2Time) {
    this.sensorType = sensorType;
    this.day = day;
    this.axle1Time = axle1Time;
    this.axle2Time = axle2Time;
    this.speed = getEstimatedSpeed(axle1Time, axle2Time);
    this.distance = OptionalInt.empty();
  }

  public Vehicle(final SensorType sensorType,
                 final int day,
                 final LocalTime axle1Time,
                 final LocalTime axle2Time,
                 final LocalTime previousAxle2Time) {
    this.sensorType = sensorType;
    this.day = day;
    this.axle1Time = axle1Time;
    this.axle2Time = axle2Time;
    this.speed = getEstimatedSpeed(axle1Time, axle2Time);
    this.distance = OptionalInt.of(getEstimatedDistance(axle1Time, previousAxle2Time, speed));
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

  public int getSpeed() {
    return speed;
  }

  public OptionalInt getDistance() {
    return distance;
  }

  private int getEstimatedSpeed(LocalTime axle1Time, LocalTime axle2Time) {
    long betweenAxles = ChronoUnit.MILLIS.between(axle1Time, axle2Time);
    if (betweenAxles <= 0) {
      throw new IllegalArgumentException("Invalid axle times");
    }
    return KMS_BETWEEN_AXLES
        .multiply(HOUR_IN_MILLIS)
        .divide(new BigDecimal(betweenAxles), 10, RoundingMode.HALF_UP)
        .setScale(0, RoundingMode.HALF_UP)
        .intValue();
  }

  private Integer getEstimatedDistance(final LocalTime axle1Time,
                                                 final LocalTime previousAxle2Time,
                                                 final int speed) {
    long betweenAxles = ChronoUnit.MILLIS.between(previousAxle2Time, axle1Time);
    if (betweenAxles <= 0) {
      throw new IllegalArgumentException("Invalid previous axle time");
    }
    return new BigDecimal(speed)
        .multiply(new BigDecimal(betweenAxles))
        .divide(HOUR_IN_MILLIS, 10, RoundingMode.HALF_UP)
        .multiply(KM_IN_METERS)
        .setScale(0, RoundingMode.HALF_UP)
        .intValue();
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
