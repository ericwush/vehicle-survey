package com.example.survey.parse

import spock.lang.Specification

import java.util.stream.Collectors

class SensorRecordProcessorSpec extends Specification {

  SensorRecordProcessor processor
  RecordParser parser
  SensorRecordRepository repository

  def setup() {
    parser = Mock()
    repository = Mock()
    processor = new SensorRecordProcessor(parser, repository)
  }

  def cleanup() {
  }

  def "test should only store parsed records"() {
    when:
    def record = Mock(SensorRecord)
    parser.parse("valid") >> Optional.of(record)
    parser.parse("invalid") >> Optional.empty()
    def input = ["valid", "invalid"].stream()
    def records = processor.parseRecords().apply(input).collect(Collectors.toList())

    then:
    records.size() == 1
    records.contains(record)
  }

  def "test should store sensor records"() {
    def records = [Mock(SensorRecord)].stream()

    when:
    processor.storeRecords().apply(records)

    then:
    1 * repository.addRecords(records)
  }

  def "test should throw exception if validation failed"() {
    when:
    def dailyRecords1 = Mock(SensorDailyRecords)
    def dailyRecords2 = Mock(SensorDailyRecords)
    dailyRecords1.validate() >> Optional.of(new Exception("error1"))
    dailyRecords2.validate() >> Optional.of(new Exception("error2"))
    processor.validateRecords().apply([dailyRecords1, dailyRecords2])

    then:
    IllegalStateException exception = thrown()
    exception.message == "error1\nerror2"
  }

  def "test should return sensors if validation passed"() {
    when:
    def dailyRecords1 = Mock(SensorDailyRecords)
    def dailyRecords2 = Mock(SensorDailyRecords)
    dailyRecords1.validate() >> Optional.empty()
    dailyRecords2.validate() >> Optional.empty()
    def result = processor.validateRecords().apply([dailyRecords1, dailyRecords2])

    then:
    result == [dailyRecords1, dailyRecords2]
  }

}
