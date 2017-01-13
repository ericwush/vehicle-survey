package com.example.survey.command;

import com.example.survey.statistics.VehicleStats;

import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Scanner;

public class CountDayCommand implements InteractiveCommand {

  private final static CommandType TYPE = CommandType.COUNT_DAY;
  private final VehicleStats stats;

  public CountDayCommand(final VehicleStats stats) {
    this.stats = stats;
  }

  @Override
  public void update(final Observable listener, final Object commandLineString) {
    if (isCommand((String) commandLineString)) {
      updateListenerAndExecute((CommandLineListener) listener);
    }
  }

  private int getDay() {
    try {
      System.out.print("day: ");
      return new Scanner(System.in).nextInt();
    } catch (NoSuchElementException e) {
      return getDay();
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
    stats.displayCountByDay(listener.getVehicles(), getInterval(), getDay());
  }

}
