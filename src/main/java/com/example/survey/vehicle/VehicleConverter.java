package com.example.survey.vehicle;

import com.example.survey.sensor.SensorDailyRecords;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * I convert sensor records to vehicle info.
 */
public class VehicleConverter {

  public Function<List<SensorDailyRecords>, Stream<Vehicle>> convert() {
    return dailyRecordsList -> dailyRecordsList.stream().map(convertDailyRecords()).flatMap(Collection::stream);
  }

  private Function<SensorDailyRecords, List<Vehicle>> convertDailyRecords() {
    return sensorDailyRecords ->
        IntStream.iterate(0, i -> i + 2).limit(sensorDailyRecords.getDailyRecords().size() / 2)
          .mapToObj(i ->
              new Vehicle(sensorDailyRecords.getType(),
                  sensorDailyRecords.getDay(),
                  sensorDailyRecords.getDailyRecords().get(i).getTime(),
                  sensorDailyRecords.getDailyRecords().get(i + 1).getTime()))
          .collect(toList());
  }

}
