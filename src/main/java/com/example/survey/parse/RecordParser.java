package com.example.survey.parse;

import java.util.Optional;

public interface RecordParser {

  Optional<SensorRecord> parse(final String input);

}
