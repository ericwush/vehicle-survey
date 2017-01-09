package com.example.survey.parse

import spock.lang.Specification

import java.time.LocalTime

class SensorARecordParserSpec extends Specification {

  SensorARecordParser parser

  def setup() {
    parser = new SensorARecordParser()
  }

  def cleanup() {
  }

  def "test pattern not matched"() {
    expect:
    parser.parse(input) == result

    where:
    input << [null, "a", "", "A0123", "A112345678", "A1b"]
    result << [Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()]
  }

  def "test pattern matched but cannot be converted to sensor"() {
    expect:
    parser.parse(input) == result

    where:
    input << ["A86400000"]
    result << [Optional.empty()]
  }

  def "test sensor converted"() {
    when:
    def sensorRecord = parser.parse(input).get()

    then:
    sensorRecord.getType() == SensorType.A
    sensorRecord.getTime() == time

    where:
    input << ["A1", "a1"]
    time << [LocalTime.of(0, 0, 0, 1000000), LocalTime.of(0, 0, 0, 1000000)]
  }

}
