package com.example.survey.command;

import com.example.survey.statistics.VehicleStats;

import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Scanner;

public class CountAverageCommand implements Command {

  private final static CommandType TYPE = CommandType.COUNT_AVERAGE;
  private final VehicleStats stats;

  public CountAverageCommand(final VehicleStats stats) {
    this.stats = stats;
  }

  @Override
  public void update(final Observable listener, final Object commandLineString) {
    if (isCommand((String) commandLineString)) {
      CommandLineListener commandLineListener = (CommandLineListener) listener;
      commandLineListener.setListenNewCommand(false);
      System.out.println(TYPE.getDescription());
      try {
        int interval = getInterval();
        stats.displayCountAverage(commandLineListener.getVehicles(), interval);
      } catch (Exception e) {
        System.out.println(e.getMessage());
        System.out.println("Back to top level menu");
      }
      ((CommandLineListener) listener).setListenNewCommand(true);
    }
  }

  private int getInterval() {
    try {
      System.out.print("interval: ");
      return new Scanner(System.in).nextInt();
    } catch (NoSuchElementException e) {
      return getInterval();
    }
  }

  @Override
  public CommandType getType() {
    return TYPE;
  }

}
