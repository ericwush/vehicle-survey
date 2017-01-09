package com.example.survey.parse

import spock.lang.Specification

import java.time.LocalTime

class SensorBRecordParserSpec extends Specification {

  SensorBRecordParser parser

  def setup() {
    parser = new SensorBRecordParser()
  }

  def cleanup() {
  }

  def "test pattern not matched"() {
    expect:
    parser.parse(input) == result

    where:
    input << [null, "b", "", "B0123", "B112345678", "B1b"]
    result << [Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()]
  }

  def "test pattern matched but cannot be converted to sensor"() {
    expect:
    parser.parse(input) == result

    where:
    input << ["B86400000"]
    result << [Optional.empty()]
  }

  def "test sensor converted"() {
    when:
    def sensorRecord = parser.parse(input).get()

    then:
    sensorRecord.getType() == SensorType.B
    sensorRecord.getTime() == time

    where:
    input << ["B1", "b1"]
    time << [LocalTime.of(0, 0, 0, 1000000), LocalTime.of(0, 0, 0, 1000000)]
  }

}
