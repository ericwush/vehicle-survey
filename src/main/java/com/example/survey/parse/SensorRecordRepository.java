package com.example.survey.parse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * I contain sensor records for all the days for all sensors.
 */
public class SensorRecordRepository {

  private final Map<SensorType, List<SensorDailyRecords>> dailyRecordsMap = new HashMap<>();

  public void addRecord(final SensorRecord record) {
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
  }

  private List<SensorDailyRecords> getSensorDailyRecordsList(final SensorRecord record) {
    return dailyRecordsMap.computeIfAbsent(record.getType(), sensorType -> {
        List<SensorDailyRecords> dailyRecordsList = new ArrayList<>();
        dailyRecordsList.add(new SensorDailyRecords(sensorType, 1));
        return dailyRecordsList;
      });
  }

  public Stream<SensorDailyRecords> getAllDailyRecords() {
    return dailyRecordsMap.values().stream().flatMap(Collection::stream);
  }

}
