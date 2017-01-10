package com.example.survey.sensor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * I store sensor records by sensor types and days.
 */
public class SensorRecordRepository {

  private Map<SensorType, List<SensorDailyRecords>> sensors = new HashMap<>();

  public List<SensorDailyRecords> addRecords(final Stream<SensorRecord> records) {
    records.forEach(this::addRecord);
    return sensors.values().stream().flatMap(Collection::stream).collect(toList());
  }

  private Map<SensorType, List<SensorDailyRecords>> addRecord(final SensorRecord record) {
    List<SensorDailyRecords> sensorDailyRecordsList = getSensorDailyRecordsList(record);
    SensorDailyRecords lastDailyRecords = sensorDailyRecordsList.get(sensorDailyRecordsList.size() - 1);
    // Determine whether the record is for a new day
    if (record.getTime().isAfter(lastDailyRecords.getLastRecordedTime())) {
      lastDailyRecords.addRecord(record);
    } else {
      SensorDailyRecords newDailyRecords = new SensorDailyRecords(record.getType(), sensorDailyRecordsList.size() + 1);
      sensorDailyRecordsList.add(newDailyRecords);
      newDailyRecords.addRecord(record);
    }
    return sensors;
  }

  private List<SensorDailyRecords> getSensorDailyRecordsList(final SensorRecord record) {
    return sensors.computeIfAbsent(record.getType(), sensorType -> {
        List<SensorDailyRecords> dailyRecordsList = new ArrayList<>();
        dailyRecordsList.add(new SensorDailyRecords(sensorType, 1));
        return dailyRecordsList;
      });
  }

}
