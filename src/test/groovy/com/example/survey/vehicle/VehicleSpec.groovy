package com.example.survey.vehicle

import com.example.survey.sensor.SensorType
import spock.lang.Specification

import java.time.LocalTime

class VehicleSpec extends Specification {

  Vehicle vehicle

  def setup() {
  }

  def cleanup() {
  }

  def "test speed"() {
    when:
    vehicle = new Vehicle(SensorType.A, 1, LocalTime.of(0, 0, 1, 0), LocalTime.of(0, 0, 1, 130000000))

    then:
    !vehicle.distance.present
    vehicle.speed == 69
  }

  def "test invalid axle times"() {
    when:
    vehicle = new Vehicle(SensorType.A, 1, LocalTime.of(0, 0, 1, 130000000), LocalTime.of(0, 0, 1, 0))

    then:
    def exception = thrown(IllegalArgumentException)
    exception.message == "Invalid axle times"
  }

  def "test distance"() {
    when:
    vehicle = new Vehicle(SensorType.A, 1, LocalTime.of(0, 0, 1, 130000000), LocalTime.of(0, 0, 1, 300000000),
        LocalTime.of(0, 0, 1, 0))

    then:
    vehicle.speed == 53
    vehicle.distance.asInt == 2
  }

  def "test invalid previous axle time"() {
    when:
    vehicle = new Vehicle(SensorType.A, 1, LocalTime.of(0, 0, 1, 130000000), LocalTime.of(0, 0, 1, 300000000),
        LocalTime.of(0, 0, 2, 0))

    then:
    def exception = thrown(IllegalArgumentException)
    exception.message == "Invalid previous axle time"
  }

}
