package com.example.survey.command;

import com.example.survey.statistics.VehicleStats;

import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Scanner;

public class PeakTimesCommand implements InteractiveCommand {

  private final static CommandType TYPE = CommandType.PEAK_TIMES;
  private final VehicleStats stats;

  public PeakTimesCommand(final VehicleStats stats) {
    this.stats = stats;
  }

  @Override
  public void update(final Observable listener, final Object commandLineString) {
    if (isCommand((String) commandLineString)) {
      updateListenerAndExecute((CommandLineListener) listener);
    }
  }

  private int getInterval() {
    try {
      System.out.print("interval in minutes: ");
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

  @Override
  public void execute(final CommandLineListener listener) {
    stats.displayPeakTimes(listener.getVehicles(), getInterval(), getTop());
  }

}
