package com.example.survey.vehicle

import com.example.survey.sensor.SensorDailyRecords
import com.example.survey.sensor.SensorRecord
import com.example.survey.sensor.SensorType
import spock.lang.Specification

import java.time.LocalTime

class VehicleProcessorSpec extends Specification {

  VehicleProcessor processor
  VehicleConverter converter
  VehicleCombiner combiner

  def setup() {
    converter = new VehicleConverter()
    combiner = new VehicleCombiner()
    processor = new VehicleProcessor(converter, combiner)
  }

  def cleanup() {
  }

  def "test process vehicle info that removes duplicates"() {
    when:
    def record1 = new SensorRecord(SensorType.A, LocalTime.of(0, 10, 0, 30000000))
    def record2 = new SensorRecord(SensorType.A, LocalTime.of(0, 10, 0, 33000000))
    def record3 = new SensorRecord(SensorType.A, LocalTime.of(1, 10, 0, 30000000))
    def record4 = new SensorRecord(SensorType.A, LocalTime.of(1, 10, 0, 33000000))
    def record5 = new SensorRecord(SensorType.B, LocalTime.of(0, 10, 0, 39000000))
    def record6 = new SensorRecord(SensorType.B, LocalTime.of(0, 10, 0, 43000000))
    def dailyRecords1 = new SensorDailyRecords(SensorType.A, 1)
    dailyRecords1.addRecord(record1)
    dailyRecords1.addRecord(record2)
    dailyRecords1.addRecord(record3)
    dailyRecords1.addRecord(record4)
    def dailyRecords2 = new SensorDailyRecords(SensorType.B, 1)
    dailyRecords2.addRecord(record5)
    dailyRecords2.addRecord(record6)

    def record7 = new SensorRecord(SensorType.A, LocalTime.of(0, 10, 0, 30000000))
    def record8 = new SensorRecord(SensorType.A, LocalTime.of(0, 10, 0, 33000000))
    def record9 = new SensorRecord(SensorType.A, LocalTime.of(1, 10, 0, 30000000))
    def record10 = new SensorRecord(SensorType.A, LocalTime.of(1, 10, 0, 33000000))
    def record11 = new SensorRecord(SensorType.B, LocalTime.of(0, 10, 0, 21000000))
    def record12 = new SensorRecord(SensorType.B, LocalTime.of(0, 10, 0, 23000000))
    def dailyRecords3 = new SensorDailyRecords(SensorType.A, 2)
    dailyRecords3.addRecord(record7)
    dailyRecords3.addRecord(record8)
    dailyRecords3.addRecord(record9)
    dailyRecords3.addRecord(record10)
    def dailyRecords4 = new SensorDailyRecords(SensorType.B, 2)
    dailyRecords4.addRecord(record11)
    dailyRecords4.addRecord(record12)

    def vehicle2 = new Vehicle(SensorType.A, 1, LocalTime.of(1, 10, 0, 30000000), LocalTime.of(1, 10, 0, 33000000))
    def vehicle3 = new Vehicle(SensorType.B, 1, LocalTime.of(0, 10, 0, 39000000), LocalTime.of(0, 10, 0, 43000000))
    def vehicle5 = new Vehicle(SensorType.A, 2, LocalTime.of(1, 10, 0, 30000000), LocalTime.of(1, 10, 0, 33000000))
    def vehicle6 = new Vehicle(SensorType.B, 2, LocalTime.of(0, 10, 0, 21000000), LocalTime.of(0, 10, 0, 23000000))

    def result = processor.process([dailyRecords1, dailyRecords2, dailyRecords3, dailyRecords4])

    then:
    result.size() == 4
    result == [vehicle2, vehicle3, vehicle5, vehicle6]
  }

}
