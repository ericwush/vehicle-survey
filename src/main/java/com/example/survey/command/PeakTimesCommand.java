package com.example.survey.command;

import com.example.survey.statistics.VehicleStats;

import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Scanner;

public class PeakTimesCommand implements Command {

  private final static CommandType TYPE = CommandType.PEAK_TIMES;
  private final VehicleStats stats;

  public PeakTimesCommand(final VehicleStats stats) {
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
        int top = getTop();
        stats.displayPeakTimes(commandLineListener.getVehicles(), interval, top);
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

  private int getTop() {
    try {
      System.out.print("peak times for top: ");
      return new Scanner(System.in).nextInt();
    } catch (NoSuchElementException e) {
      return getTop();
    }
  }

  @Override
  public CommandType getType() {
    return TYPE;
  }

}
