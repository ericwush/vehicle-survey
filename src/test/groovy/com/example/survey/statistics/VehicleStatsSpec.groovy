package com.example.survey.statistics

import com.example.survey.sensor.SensorType
import com.example.survey.vehicle.Vehicle
import spock.lang.Specification

import java.time.LocalTime

class VehicleStatsSpec extends Specification {

  VehicleStats stats

  def setup() {
    stats = new VehicleStats()
  }

  def cleanup() {
  }

  def "test count by day"() {
    when:
    def v1 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 1, 5), LocalTime.of(0, 1, 6))
    def v2 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 31, 5), LocalTime.of(0, 31, 6))
    def v3 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 41, 5), LocalTime.of(0, 41, 6))
    def v4 = new Vehicle(SensorType.B, 1, LocalTime.of(1, 0, 5), LocalTime.of(1, 0, 6))
    def v5 = new Vehicle(SensorType.B, 1, LocalTime.of(2, 0, 5), LocalTime.of(2, 0, 6))

    def v6 = new Vehicle(SensorType.A, 2, LocalTime.of(0, 41, 5), LocalTime.of(0, 41, 6))
    def v7 = new Vehicle(SensorType.B, 2, LocalTime.of(1, 0, 5), LocalTime.of(1, 0, 6))

    def result = stats.countByDay([v1, v2, v3, v4, v5, v6, v7], 30, 1)

    then:
    result.get(SensorType.A).size() == 2
    result.get(SensorType.A).get(0).intValue() == 1
    result.get(SensorType.A).get(1).intValue() == 2
    result.get(SensorType.B).size() == 2
    result.get(SensorType.B).get(2).intValue() == 1
    result.get(SensorType.B).get(4).intValue() == 1
  }

  def "test count by day exceeds max day"() {
    when:
    def v1 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 1, 5), LocalTime.of(0, 1, 6))
    stats.countByDay([v1], 30, 2)

    then:
    def exception = thrown(IllegalArgumentException)
    exception.message == "Input day 2 exceeds max day 1"
  }

  def "test count average"() {
    when:
    def v1 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 1, 5), LocalTime.of(0, 1, 6))
    def v2 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 31, 5), LocalTime.of(0, 31, 6))
    def v3 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 41, 5), LocalTime.of(0, 41, 6))
    def v4 = new Vehicle(SensorType.B, 1, LocalTime.of(1, 0, 5), LocalTime.of(1, 0, 6))
    def v5 = new Vehicle(SensorType.B, 1, LocalTime.of(2, 0, 5), LocalTime.of(2, 0, 6))

    def v6 = new Vehicle(SensorType.A, 2, LocalTime.of(0, 35, 5), LocalTime.of(0, 35, 6))
    def v7 = new Vehicle(SensorType.B, 2, LocalTime.of(1, 0, 5), LocalTime.of(1, 0, 6))

    def result = stats.countAverage([v1, v2, v3, v4, v5, v6, v7], 30)

    then:
    result.get(SensorType.A).size() == 2
    result.get(SensorType.A).get(0).intValue() == 1
    result.get(SensorType.A).get(1).intValue() == 2
    result.get(SensorType.B).size() == 2
    result.get(SensorType.B).get(2).intValue() == 1
    result.get(SensorType.B).get(4).intValue() == 1
  }

  def "test peak times"() {
    when:
    def v1 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 1, 5), LocalTime.of(0, 1, 6))
    def v2 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 31, 5), LocalTime.of(0, 31, 6))
    def v3 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 41, 5), LocalTime.of(0, 41, 6))
    def v4 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 42, 5), LocalTime.of(0, 42, 6))
    def v5 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 43, 5), LocalTime.of(0, 43, 6))
    def v6 = new Vehicle(SensorType.A, 1, LocalTime.of(1, 1, 5), LocalTime.of(1, 1, 6))
    def v7 = new Vehicle(SensorType.A, 1, LocalTime.of(1, 11, 5), LocalTime.of(1, 11, 6))
    def v8 = new Vehicle(SensorType.B, 1, LocalTime.of(1, 0, 5), LocalTime.of(1, 0, 6))
    def v9 = new Vehicle(SensorType.B, 1, LocalTime.of(2, 0, 5), LocalTime.of(2, 0, 6))
    def v10 = new Vehicle(SensorType.B, 1, LocalTime.of(3, 0, 5), LocalTime.of(3, 0, 6))

    def result = stats.peakTimes([v1, v2, v3, v4, v5, v6, v7, v8, v9, v10], 30, 2)

    then:
    result.get(SensorType.A).size() == 2
    result.get(SensorType.A).get(1).intValue() == 4
    result.get(SensorType.A).get(2).intValue() == 2
    result.get(SensorType.B).size() == 2
    result.get(SensorType.B).get(2).intValue() == 1
    result.get(SensorType.B).get(4).intValue() == 1
  }

  def "test speed"() {
    when:
    def v1 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 0, 1, 0), LocalTime.of(0, 0, 1, 130000000))
    def v2 = new Vehicle(SensorType.A, 2, LocalTime.of(0, 0, 1, 130000000), LocalTime.of(0, 0, 1, 300000000))
    def v3 = new Vehicle(SensorType.B, 1, LocalTime.of(0, 40, 1, 130000000), LocalTime.of(0, 40, 1, 300000000))
    def v4 = new Vehicle(SensorType.B, 2, LocalTime.of(0, 40, 1, 330000000), LocalTime.of(0, 40, 1, 550000000))

    def result = stats.speed([v1, v2, v3, v4], 30)

    then:
    result.get(SensorType.A).size() == 1
    result.get(SensorType.A).get(0).intValue() == 61
    result.get(SensorType.B).size() == 1
    result.get(SensorType.B).get(1).intValue() == 47
  }

  def "test distance"() {
    when:
    def v1 = new Vehicle(SensorType.A, 1, LocalTime.of(0, 0, 1, 130000000), LocalTime.of(0, 0, 1, 300000000),
        LocalTime.of(0, 0, 1, 0))
    def v2 = new Vehicle(SensorType.A, 2, LocalTime.of(0, 0, 1, 130000000), LocalTime.of(0, 0, 1, 300000000),
        LocalTime.of(0, 0, 0, 80000000))
    def v3 = new Vehicle(SensorType.B, 1, LocalTime.of(0, 40, 1, 130000000), LocalTime.of(0, 40, 1, 300000000),
        LocalTime.of(0, 0, 0, 70000000))
    def v4 = new Vehicle(SensorType.B, 2, LocalTime.of(0, 40, 1, 330000000), LocalTime.of(0, 40, 1, 550000000),
        LocalTime.of(0, 0, 1, 0))

    def result = stats.distance([v1, v2, v3, v4], 30)

    then:
    result.get(SensorType.A).size() == 1
    result.get(SensorType.A).get(0).intValue() == 8
    result.get(SensorType.B).size() == 1
    result.get(SensorType.B).get(1).intValue() == 31343
  }

}
