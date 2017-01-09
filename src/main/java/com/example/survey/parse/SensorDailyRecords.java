package com.example.survey.parse;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * I contain daily sensor records for a certain sensor.
 */
public class SensorDailyRecords {

  private final List<SensorRecord> dailyRecords = new ArrayList<>();

  private final SensorType type;
  private final int day;
  private LocalTime lastRecordedTime = LocalTime.of(0, 0);

  public SensorDailyRecords(final SensorType type, final int day) {
    this.type = type;
    this.day = day;
  }

  public void addRecord(SensorRecord record) {
    if (record.getType().equals(type)) {
      dailyRecords.add(record);
      lastRecordedTime = record.getTime();
    }
  }

  public Optional<Throwable> validate() {
    if (dailyRecords.size() % 2 != 0) {
      return Optional.of(new IllegalArgumentException("The number of records is uneven for Sensor " + type + " day " + day));
    }
    return Optional.empty();
  }

  public LocalTime getLastRecordedTime() {
    return lastRecordedTime;
  }

  public int getDay() {
    return day;
  }

  public SensorType getType() {
    return type;
  }

  public List<SensorRecord> getDailyRecords() {
    return dailyRecords;
  }

}
