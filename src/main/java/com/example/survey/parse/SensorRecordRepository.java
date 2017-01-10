package com.example.survey.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * I store sensor records by sensor types and days.
 */
public class SensorRecordRepository {

  public Map<SensorType, List<SensorDailyRecords>> addRecords(final Stream<SensorRecord> records,
                                                              final Map<SensorType, List<SensorDailyRecords>> sensors) {
    records.forEach(record -> this.addRecord(record, sensors));
    return sensors;
  }

  private Map<SensorType, List<SensorDailyRecords>> addRecord(final SensorRecord record,
                                                              final Map<SensorType, List<SensorDailyRecords>> sensors) {
    List<SensorDailyRecords> sensorDailyRecordsList = getSensorDailyRecordsList(record, sensors);
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

  private List<SensorDailyRecords> getSensorDailyRecordsList(final SensorRecord record,
                                                             final Map<SensorType, List<SensorDailyRecords>> sensors) {
    return sensors.computeIfAbsent(record.getType(), sensorType -> {
        List<SensorDailyRecords> dailyRecordsList = new ArrayList<>();
        dailyRecordsList.add(new SensorDailyRecords(sensorType, 1));
        return dailyRecordsList;
      });
  }

}
