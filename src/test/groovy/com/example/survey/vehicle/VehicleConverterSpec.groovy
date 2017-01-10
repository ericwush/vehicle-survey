package com.example.survey.vehicle

import com.example.survey.sensor.SensorDailyRecords
import com.example.survey.sensor.SensorRecord
import com.example.survey.sensor.SensorType
import spock.lang.Specification

import java.time.LocalTime
import java.util.stream.Collectors

class VehicleConverterSpec extends Specification {

  VehicleConverter converter

  def setup() {
    converter = new VehicleConverter()
  }

  def cleanup() {
  }

  def "test convert sensor records to vehicles"() {
    when:
    def record1 = new SensorRecord(sensorType, LocalTime.of(1, 2))
    def record2 = new SensorRecord(sensorType, LocalTime.of(3, 4))
    def record3 = new SensorRecord(sensorType, LocalTime.of(5, 6))
    def record4 = new SensorRecord(sensorType, LocalTime.of(7, 8))
    def dailyRecords = new SensorDailyRecords(sensorType, day)
    dailyRecords.addRecord(record1)
    dailyRecords.addRecord(record2)
    dailyRecords.addRecord(record3)
    dailyRecords.addRecord(record4)
    def vehicles = converter.convert().apply([dailyRecords]).collect(Collectors.toList())
    def vehicle1 = new Vehicle(sensorType, day, LocalTime.of(1, 2), LocalTime.of(3, 4))
    def vehicle2 = new Vehicle(sensorType, day, LocalTime.of(5, 6), LocalTime.of(7, 8))

    then:
    vehicles.size() == 2
    vehicles.contains(vehicle1)
    vehicles.contains(vehicle2)

    where:
    sensorType = SensorType.A
    day = 2
  }

}
