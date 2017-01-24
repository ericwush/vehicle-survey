package com.example.survey.vehicle;

import com.example.survey.sensor.SensorDailyRecords;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * I process vehicle info and remove the duplicates for the ones that are recorded by both sensors.
 */
public class VehicleProcessor {

  private final VehicleConverter converter;
  private final VehicleCombiner combiner;

  public VehicleProcessor(final VehicleConverter converter, final VehicleCombiner combiner) {
    this.converter = converter;
    this.combiner = combiner;
  }

  public List<Vehicle> process(final List<SensorDailyRecords> sensorRecords) {
    return Collections.unmodifiableList(
        converter.convert()
            .andThen(groupVehiclesByDays())
            .andThen(combineVehiclesByDays())
            .andThen(joinFutureVehicles())
            .apply(sensorRecords));
  }

  private Function<Stream<Vehicle>, Stream<List<Vehicle>>> groupVehiclesByDays() {
    return vehicles -> vehicles.collect(groupingBy(Vehicle::getDay)).values().stream();
  }

  private Function<Stream<List<Vehicle>>, List<CompletableFuture<List<Vehicle>>>> combineVehiclesByDays() {
    return vehiclesByDays ->
        vehiclesByDays
            .map(vehicles -> CompletableFuture.supplyAsync(combiner.combine(vehicles)))
            .collect(toList());
  }

  private Function<List<CompletableFuture<List<Vehicle>>>, List<Vehicle>> joinFutureVehicles() {
    return futureVehicles -> {
      CompletableFuture<Void> allDoneFuture =
          CompletableFuture.allOf(futureVehicles.toArray(new CompletableFuture[futureVehicles.size()]));
      CompletableFuture<List<List<Vehicle>>> futureVehicleLists = allDoneFuture.thenApply(v ->
          futureVehicles.stream().
              map(CompletableFuture::join).
              collect(toList()));
      List<Vehicle> joinedVehicles;
      try {
        joinedVehicles = futureVehicleLists.get().stream().flatMap(Collection::stream).collect(toList());
      } catch (InterruptedException | ExecutionException e) {
        throw new IllegalStateException("Error occurred when processing vehicle info");
      }
      return joinedVehicles;
    };
  }

}
