package com.example.survey.vehicle;

import com.example.survey.sensor.SensorType;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * I remove the duplicates for the ones that are recorded by both sensors for the same day.
 */
public class VehicleCombiner {

  // This can be externalized to configuration if required
  private static final int BUFFER_BETWEEN_SENSORS_IN_MILLIS = 10;

  public Supplier<List<Vehicle>> combine(List<Vehicle> vehicles) {
    return () -> vehicles.stream().collect(ArrayList::new, accumulator(), ArrayList::addAll);
  }

  private BiConsumer<ArrayList<Vehicle>, ? super Vehicle> accumulator() {
    // Find vehicles recorded by both sensors and decide which ones should be stored based on sensor type
    return (vehicles, vehicle) -> {
      // If the 1st axle time difference is within the buffer range, it is regarded as duplicate
      Optional<Vehicle> maybeDuplicate = vehicles.stream()
          .filter(v ->
              (v.getDay() == vehicle.getDay())
                  && (vehicle.getAxle1Time().isBefore(v.getAxle1Time().plus(BUFFER_BETWEEN_SENSORS_IN_MILLIS, ChronoUnit.MILLIS)))
                  && (vehicle.getAxle1Time().isAfter(v.getAxle1Time().minus(BUFFER_BETWEEN_SENSORS_IN_MILLIS, ChronoUnit.MILLIS))))
          .findFirst();
      if (maybeDuplicate.isPresent()) {
        if (maybeDuplicate.get().getSensorType().equals(SensorType.A)) {
          vehicles.remove(maybeDuplicate.get());
          vehicles.add(vehicle);
        }
      } else {
        vehicles.add(vehicle);
      }
    };
  }

}
