package com.example.survey.command;

import java.util.Arrays;
import java.util.Optional;

public enum CommandType {

  MENU(0, "0 - Menu"), COUNT_DAY(1, "1 - Count by day"), COUNT_AVERAGE(2, "2 - Count average"),
  PEAK_TIMES(3, "3 - Peak volume times"), SPEED(4, "4 - Speed"), DISTANCE(5, "5 - Distance");

  private final int code;
  private final String description;

  CommandType(final int code, final String description) {
    this.code = code;
    this.description = description;
  }

  public static Optional<CommandType> getByCode(final String code) {
    Integer parsedCode;
    try {
      parsedCode = Integer.valueOf(code);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
    return Arrays.stream(CommandType.values()).filter(type -> type.code == parsedCode).findFirst();
  }

  public String getDescription() {
    return description;
  }

}
