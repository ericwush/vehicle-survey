package com.example.survey.sensor;

import java.util.Optional;

public interface RecordParser {

  Optional<SensorRecord> parse(final String input);

}
