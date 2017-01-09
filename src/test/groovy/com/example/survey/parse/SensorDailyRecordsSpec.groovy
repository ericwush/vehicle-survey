package com.example.survey.parse

import spock.lang.Specification

import java.time.LocalTime

class SensorDailyRecordsSpec extends Specification {

  SensorDailyRecords dailyRecords

  def setup() {
  }

  def cleanup() {
  }

  def "test constructor"() {
    when:
    dailyRecords = new SensorDailyRecords(SensorType.B, 3)

    then:
    dailyRecords.getType() == SensorType.B
    dailyRecords.getDay() == 3
  }

  def "test add record with same sensor type"() {
    when:
    dailyRecords = new SensorDailyRecords(SensorType.A, 3)
    dailyRecords.addRecord(new SensorRecord(SensorType.A, LocalTime.of(1, 3)))

    then:
    dailyRecords.getDailyRecords().size() == 1
    dailyRecords.getDailyRecords().contains(new SensorRecord(SensorType.A, LocalTime.of(1, 3)))
    dailyRecords.getLastRecordedTime() == LocalTime.of(1, 3)
  }

  def "test add record with different sensor type"() {
    when:
    dailyRecords = new SensorDailyRecords(SensorType.B, 3)
    dailyRecords.addRecord(new SensorRecord(SensorType.A, LocalTime.of(1, 3)))

    then:
    dailyRecords.getDailyRecords().size() == 0
    !dailyRecords.getDailyRecords().contains(new SensorRecord(SensorType.A, LocalTime.of(1, 3)))
    dailyRecords.getLastRecordedTime() == LocalTime.of(0, 0)
  }

  def "test validate empty records"() {
    when:
    dailyRecords = new SensorDailyRecords(SensorType.B, 3)

    then:
    dailyRecords.validate() == Optional.empty()
  }

  def "test validate even number of records"() {
    when:
    dailyRecords = new SensorDailyRecords(SensorType.A, 3)
    dailyRecords.addRecord(new SensorRecord(SensorType.A, LocalTime.of(1, 3)))
    dailyRecords.addRecord(new SensorRecord(SensorType.A, LocalTime.of(2, 3)))

    then:
    dailyRecords.validate() == Optional.empty()
  }

  def "test validate uneven number of records"() {
    when:
    dailyRecords = new SensorDailyRecords(SensorType.A, 3)
    dailyRecords.addRecord(new SensorRecord(SensorType.A, LocalTime.of(1, 3)))
    def throwable = dailyRecords.validate().get()

    then:
    throwable.class == IllegalArgumentException
    throwable.message == "The number of records is uneven for Sensor A day 3"
  }

}
