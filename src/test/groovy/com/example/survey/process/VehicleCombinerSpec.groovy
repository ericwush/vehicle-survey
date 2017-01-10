package com.example.survey.process

import com.example.survey.parse.SensorType
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
    def vehicle1 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 10, 0, 1000030), LocalTime.of(0, 10, 0, 1000033))
    def vehicle2 = new Vehicle(SensorType.A, 1, LocalTime.of(1, 10, 0, 1000030), LocalTime.of(1, 10, 0, 1000033))
    def vehicle3 = new Vehicle(SensorType.B, 1, LocalTime.of(0, 10, 0, 1000039), LocalTime.of(0, 10, 0, 1000043))
    def vehicle4 = new Vehicle(SensorType.A, 2, LocalTime.of(0, 10, 0, 1000030), LocalTime.of(0, 10, 0, 1000033))
    def vehicle5 = new Vehicle(SensorType.A, 2, LocalTime.of(1, 10, 0, 1000030), LocalTime.of(1, 10, 0, 1000033))
    def vehicle6 = new Vehicle(SensorType.B, 2, LocalTime.of(0, 10, 0, 1000021), LocalTime.of(0, 10, 0, 1000023))

    def result = combiner.combine([vehicle1, vehicle2, vehicle3, vehicle4, vehicle5, vehicle6]).get()

    then:
    result.size() == 4
    result == [vehicle2, vehicle3, vehicle5, vehicle6]
  }

}
