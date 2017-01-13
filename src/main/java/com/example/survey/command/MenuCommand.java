package com.example.survey.command;

import java.util.Arrays;
import java.util.Observable;

public class MenuCommand implements Command {

  private final static CommandType TYPE = CommandType.MENU;

  @Override
  public void update(final Observable listener, final Object commandLineString) {
    if (isCommand((String) commandLineString)) {
      Arrays.stream(CommandType.values()).forEach(commandType -> System.out.println(commandType.getDescription()));
    }
  }

  @Override
  public CommandType getType() {
    return TYPE;
  }

}
