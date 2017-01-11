package com.example.survey.vehicle

import com.example.survey.sensor.SensorType
import spock.lang.Specification

import java.time.LocalTime

class VehicleCombinerSpec extends Specification {

  VehicleCombiner combiner

  def setup() {
    combiner = new VehicleCombiner()
  }

  def cleanup() {
  }

  def "test remove the duplicates for the ones that are recorded by both sensors for the same day"() {
    when:
    def vehicle1 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 10, 0, 30000000), LocalTime.of(0, 10, 0, 33000000))
    def vehicle2 = new Vehicle(SensorType.A, 1, LocalTime.of(1, 10, 0, 30000000), LocalTime.of(1, 10, 0, 33000000))
    def vehicle3 = new Vehicle(SensorType.B, 1, LocalTime.of(0, 10, 0, 39000000), LocalTime.of(0, 10, 0, 43000000))
    def vehicle4 = new Vehicle(SensorType.A, 2, LocalTime.of(0, 10, 0, 30000000), LocalTime.of(0, 10, 0, 33000000))
    def vehicle5 = new Vehicle(SensorType.A, 2, LocalTime.of(1, 10, 0, 30000000), LocalTime.of(1, 10, 0, 33000000))
    def vehicle6 = new Vehicle(SensorType.B, 2, LocalTime.of(0, 10, 0, 21000000), LocalTime.of(0, 10, 0, 23000000))

    def result = combiner.combine([vehicle1, vehicle2, vehicle3, vehicle4, vehicle5, vehicle6]).get()

    then:
    result.size() == 4
    result == [vehicle2, vehicle3, vehicle5, vehicle6]
  }

}
