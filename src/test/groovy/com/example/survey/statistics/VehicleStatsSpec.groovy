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

}
