package com.example.survey.parse

import spock.lang.Specification

import java.time.LocalTime

class SensorRecordRepositorySpec extends Specification {

  SensorRecordRepository repository
  Map<SensorType, List<SensorDailyRecords>> sensors

  def setup() {
    repository = new SensorRecordRepository()
    sensors = new HashMap<>()
  }

  def cleanup() {
  }

  def "test add records for different sensor types"() {
    when:
    def sensorRecords = [new SensorRecord(SensorType.A, LocalTime.of(1, 2)),
                         new SensorRecord(SensorType.B, LocalTime.of(3, 4))].stream()
    repository.addRecords(sensorRecords, sensors)

    def records = sensors.values()
    def dailyRecords1 = new SensorDailyRecords(SensorType.A, 1)
    dailyRecords1.addRecord(new SensorRecord(SensorType.A, LocalTime.of(1, 2)))
    def dailyRecords2 = new SensorDailyRecords(SensorType.B, 1)
    dailyRecords2.addRecord(new SensorRecord(SensorType.B, LocalTime.of(3, 4)))

    then:
    records.size() == 2
    records.contains([dailyRecords1])
    records.contains([dailyRecords2])
  }

  def "test add records for different days"() {
    when:
    def sensorRecords = [new SensorRecord(SensorType.A, LocalTime.of(3, 4)),
                         new SensorRecord(SensorType.A, LocalTime.of(1, 2))].stream()
    repository.addRecords(sensorRecords, sensors)

    def records = sensors.values()
    def dailyRecords1 = new SensorDailyRecords(SensorType.A, 1)
    dailyRecords1.addRecord(new SensorRecord(SensorType.A, LocalTime.of(3, 4)))
    def dailyRecords2 = new SensorDailyRecords(SensorType.A, 2)
    dailyRecords2.addRecord(new SensorRecord(SensorType.A, LocalTime.of(1, 2)))

    then:
    records.size() == 1
    records.contains([dailyRecords1, dailyRecords2])
  }

}
