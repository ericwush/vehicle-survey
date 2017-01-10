package com.example.survey.process;

import com.example.survey.parse.SensorDailyRecords;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * I convert sensor record to vehicle info.
 */
public class VehicleConverter implements Function<SensorDailyRecords, List<Vehicle>> {

  @Override
  public List<Vehicle> apply(final SensorDailyRecords sensorDailyRecords) {
    return IntStream.iterate(0, i -> i + 2).limit(sensorDailyRecords.getDailyRecords().size() / 2)
        .mapToObj(i ->
            new Vehicle(sensorDailyRecords.getType(),
                sensorDailyRecords.getDay(),
                sensorDailyRecords.getDailyRecords().get(i).getTime(),
                sensorDailyRecords.getDailyRecords().get(i + 1).getTime()))
        .collect(toList());
  }

}
