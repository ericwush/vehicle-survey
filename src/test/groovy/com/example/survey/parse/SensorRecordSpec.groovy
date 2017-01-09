package com.example.survey.parse

import spock.lang.Specification

import java.time.LocalTime

class SensorRecordSpec extends Specification {

  SensorRecord record

  def setup() {
  }

  def cleanup() {
  }

  def "test constructor"() {
    when:
    record = new SensorRecord(SensorType.A, LocalTime.of(2, 1, 1))

    then:
    record.getType() == SensorType.A
    record.getTime() == LocalTime.of(2, 1, 1)
  }

}
