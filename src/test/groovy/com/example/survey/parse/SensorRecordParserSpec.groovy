package com.example.survey.parse

import spock.lang.Specification

class SensorRecordParserSpec extends Specification {

  SensorRecordParser parser
  RecordParser parser1
  RecordParser parser2
  RecordParser parser3

  def setup() {
    parser1 = Mock()
    parser2 = Mock()
    parser3 = Mock()
    parser = new SensorRecordParser(parser1, parser2, parser3)
  }

  def cleanup() {
  }

  def "test cannot be parsed"() {
    when:
    parser1.parse("input") >> Optional.empty()
    parser2.parse("input") >> Optional.empty()
    parser3.parse("input") >> Optional.empty()

    then:
    parser.parse("input") == Optional.empty()
  }

  def "test parse get sensor record"() {
    when:
    SensorRecord sensorRecord = Mock()
    parser1.parse("input") >> Optional.empty()
    parser2.parse("input") >> Optional.of(sensorRecord)
    def result = parser.parse("input")

    then:
    result == Optional.of(sensorRecord)
    0 * parser3.parse("input")
  }

}
