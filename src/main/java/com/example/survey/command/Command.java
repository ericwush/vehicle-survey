package com.example.survey.command;

import java.util.Observer;
import java.util.Optional;

public interface Command extends Observer {

  CommandType getType();

  default boolean isCommand(String commandLineString) {
    Optional<CommandType> mayBeCommandType = CommandType.getByCode(commandLineString.trim());
    return mayBeCommandType.filter(type -> type.equals(getType())).isPresent();
  }

}
