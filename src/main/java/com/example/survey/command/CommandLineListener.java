package com.example.survey.command;

import com.example.survey.vehicle.Vehicle;

import java.util.List;
import java.util.Observable;
import java.util.Scanner;

/**
 * I listen to the command line and notify on input.
 */
public class CommandLineListener extends Observable implements Runnable {

  private boolean listenNewCommand = true;
  private boolean exit = false;

  private final List<Vehicle> vehicles;

  public CommandLineListener(final List<Vehicle> vehicles) {
    this.vehicles = vehicles;
  }

  @Override
  public void run() {
    while (!exit) {
      if (listenNewCommand) {
        String commandLineString = new Scanner(System.in).nextLine();
        setChanged();
        notifyObservers(commandLineString);
      }
    }
  }

  public List<Vehicle> getVehicles() {
    return vehicles;
  }

  public void setListenNewCommand(final boolean listenNewCommand) {
    this.listenNewCommand = listenNewCommand;
  }

  public void setExit(final boolean exit) {
    this.exit = exit;
  }

}
