package com.example.survey.command;

import java.util.Observable;

public class ExitCommand implements Command {

  private final static CommandType TYPE = CommandType.EXIT;

  @Override
  public void update(final Observable listener, final Object commandLineString) {
    if (isCommand((String) commandLineString)) {
      ((CommandLineListener) listener).setExit(true);
    }
  }

  @Override
  public CommandType getType() {
    return TYPE;
  }

}
