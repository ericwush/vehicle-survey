package com.example.survey.command;

import com.example.survey.statistics.VehicleStats;

import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Scanner;

public class DistanceCommand implements InteractiveCommand {

  private final static CommandType TYPE = CommandType.DISTANCE;
  private final VehicleStats stats;

  public DistanceCommand(final VehicleStats stats) {
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

  @Override
  public CommandType getType() {
    return TYPE;
  }

  @Override
  public void execute(final CommandLineListener listener) {
    stats.displayDistance(listener.getVehicles(), getInterval());
  }

}
