package com.example.survey.parse

import spock.lang.Specification

import java.time.LocalTime
import java.util.stream.Collectors

class SensorRecordRepositorySpec extends Specification {

  SensorRecordRepository repository

  def setup() {
    repository = new SensorRecordRepository()
  }

  def cleanup() {
  }

  def "test add records for different sensor types"() {
    when:
    repository.addRecord(new SensorRecord(SensorType.A, LocalTime.of(1, 2)))
    repository.addRecord(new SensorRecord(SensorType.B, LocalTime.of(3, 4)))

    def records = repository.getAllDailyRecords().collect(Collectors.toList())
    def dailyRecords1 = new SensorDailyRecords(SensorType.A, 1)
    dailyRecords1.addRecord(new SensorRecord(SensorType.A, LocalTime.of(1, 2)))
    def dailyRecords2 = new SensorDailyRecords(SensorType.B, 1)
    dailyRecords2.addRecord(new SensorRecord(SensorType.B, LocalTime.of(3, 4)))

    then:
    records.size() == 2
    records.contains(dailyRecords1)
    records.contains(dailyRecords2)
  }

  def "test add records for different days"() {
    when:
    repository.addRecord(new SensorRecord(SensorType.A, LocalTime.of(3, 4)))
    repository.addRecord(new SensorRecord(SensorType.A, LocalTime.of(1, 2)))

    def records = repository.getAllDailyRecords().collect(Collectors.toList())
    def dailyRecords1 = new SensorDailyRecords(SensorType.A, 1)
    dailyRecords1.addRecord(new SensorRecord(SensorType.A, LocalTime.of(3, 4)))
    def dailyRecords2 = new SensorDailyRecords(SensorType.A, 2)
    dailyRecords2.addRecord(new SensorRecord(SensorType.A, LocalTime.of(1, 2)))

    then:
    records.size() == 2
    records.contains(dailyRecords1)
    records.contains(dailyRecords2)
  }


}
