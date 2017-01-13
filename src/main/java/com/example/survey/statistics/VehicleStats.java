package com.example.survey.statistics;

import com.example.survey.sensor.SensorType;
import com.example.survey.vehicle.Vehicle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class VehicleStats {

  // I would have introduced Javaslang lib which provides Tuples
  // so it's easier to have richer info in return types
  // e.g. it can self contain intervalInMinutes and interpret the numbers representing the session
  // of a day to be more descriptive (present as time range)

  public Map<SensorType, Map<Integer, Long>> countByDay(final List<Vehicle> vehicles,
                                                        final int intervalInMinutes,
                                                        final int day) {
    int days = vehicles.stream().collect(groupingBy(Vehicle::getDay)).keySet().size();
    if (day > days) {
      throw new IllegalArgumentException("Input day " + day + " exceeds max day " + days);
    }
    return countAll(
        vehicles.stream()
            .collect(groupingBy(Vehicle::getDay))
            .get(day),
        intervalInMinutes);
  }

  public Map<SensorType, Map<Integer, Long>> countAverage(final List<Vehicle> vehicles, final int intervalInMinutes) {
    int days = vehicles.stream().collect(groupingBy(Vehicle::getDay)).keySet().size();
    Map<SensorType, Map<Integer, Long>> all = countAll(vehicles, intervalInMinutes);
    return all.entrySet().stream()
        .map(entry -> {
          entry.getValue().entrySet().forEach(statsEntry -> {
            BigDecimal sum = new BigDecimal(statsEntry.getValue());
            BigDecimal avg = sum.divide(new BigDecimal(days), RoundingMode.HALF_UP).setScale(0, BigDecimal.ROUND_HALF_UP);
            statsEntry.setValue(avg.longValue());
          });
          return entry;
        })
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue
        ));
  }

  private Map<SensorType, Map<Integer, Long>> countAll(final List<Vehicle> vehicles, final int intervalInMinutes) {
    return vehicles.stream()
        .collect(
            groupingBy(Vehicle::getSensorType,
                groupingBy(v -> v.getAxle1Time().get(ChronoField.MINUTE_OF_DAY) / intervalInMinutes,
                    Collectors.counting())
            ));
  }

  public Map<SensorType, Map<Integer, Long>> peakTimes(final List<Vehicle> vehicles,
                                                       final int intervalInMinutes,
                                                       final int top) {
    Map<SensorType, Map<Integer, Long>> statsByType = countAverage(vehicles, intervalInMinutes);
    return statsByType.entrySet().stream()
        .map(entry -> {
              LinkedHashMap<Integer, Long> sortedStats = entry.getValue().entrySet()
                  .stream()
                  .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                  .limit(top)
                  .collect(Collectors.toMap(
                      Map.Entry::getKey,
                      Map.Entry::getValue,
                      (e1, e2) -> e1,
                      LinkedHashMap::new
                  ));
              entry.setValue(sortedStats);
              return entry;
            })
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue
        ));
  }

  public Map<SensorType, Map<Integer, Double>> speed(final List<Vehicle> vehicles, final int intervalInMinutes) {
    return vehicles.stream()
        .collect(
            groupingBy(Vehicle::getSensorType,
                groupingBy(v -> v.getAxle1Time().get(ChronoField.MINUTE_OF_DAY) / intervalInMinutes,
                    Collectors.averagingLong(Vehicle::getSpeed))
            ));
  }

  public Map<SensorType, Map<Integer, Double>> distance(final List<Vehicle> vehicles, final int intervalInMinutes) {
    return vehicles.stream()
        .filter(vehicle -> vehicle.getDistance().isPresent())
        .collect(
            groupingBy(Vehicle::getSensorType,
                groupingBy(v -> v.getAxle1Time().get(ChronoField.MINUTE_OF_DAY) / intervalInMinutes,
                    Collectors.averagingLong(vehicle -> vehicle.getDistance().getAsInt()))
            ));
  }

  private <T extends Number> void display(final Map<SensorType, Map<Integer, T>> stats, final int intervalInMinutes) {
    stats.forEach((sensorType, statsMap) ->
        statsMap.forEach((slot, result) -> {
          LocalTime start = LocalTime.of(0, 0).plus(slot * intervalInMinutes, ChronoUnit.MINUTES);
          LocalTime end = start.plus(intervalInMinutes, ChronoUnit.MINUTES);
          System.out.println(sensorType.getDirection() + " - [" + start + " - " + end + "] - " + result.intValue());
        })
    );
  }

  public void displayCountByDay(final List<Vehicle> vehicles,
                                final int intervalInMinutes,
                                final int day) {
    display(countByDay(vehicles, intervalInMinutes, day), intervalInMinutes);
  }

  public void displayCountAverage(final List<Vehicle> vehicles, final int intervalInMinutes) {
    display(countAverage(vehicles, intervalInMinutes), intervalInMinutes);
  }

  public void displayPeakTimes(final List<Vehicle> vehicles, final int intervalInMinutes, final int top) {
    display(peakTimes(vehicles, intervalInMinutes, top), intervalInMinutes);
  }

  public void displaySpeed(final List<Vehicle> vehicles, final int intervalInMinutes) {
    display(speed(vehicles, intervalInMinutes), intervalInMinutes);
  }

  public void displayDistance(final List<Vehicle> vehicles, final int intervalInMinutes) {
    display(distance(vehicles, intervalInMinutes), intervalInMinutes);
  }

}
