package com.example.survey;

import com.example.survey.command.CommandLineListener;
import com.example.survey.command.CommandType;
import com.example.survey.command.CountAverageCommand;
import com.example.survey.command.CountDayCommand;
import com.example.survey.command.DistanceCommand;
import com.example.survey.command.ExitCommand;
import com.example.survey.command.MenuCommand;
import com.example.survey.command.PeakTimesCommand;
import com.example.survey.command.SpeedCommand;
import com.example.survey.sensor.SensorARecordParser;
import com.example.survey.sensor.SensorBRecordParser;
import com.example.survey.sensor.SensorDailyRecords;
import com.example.survey.sensor.SensorRecordFileParser;
import com.example.survey.sensor.SensorRecordParser;
import com.example.survey.sensor.SensorRecordProcessor;
import com.example.survey.sensor.SensorRecordRepository;
import com.example.survey.statistics.VehicleStats;
import com.example.survey.vehicle.Vehicle;
import com.example.survey.vehicle.VehicleCombiner;
import com.example.survey.vehicle.VehicleConverter;
import com.example.survey.vehicle.VehicleProcessor;

import java.util.Arrays;
import java.util.List;

public class VehicleSurvey {

  // main entrance of the program
  public static void main(String[] args) throws Exception {
    // parse file
    String filename = args[0];
    SensorRecordParser recordParser = new SensorRecordParser(new SensorARecordParser(), new SensorBRecordParser());
    SensorRecordProcessor recordProcessor = new SensorRecordProcessor(recordParser, new SensorRecordRepository());
    SensorRecordFileParser fileParser = new SensorRecordFileParser(recordProcessor);
    List<SensorDailyRecords> sensorRecords = fileParser.parse(filename);
    VehicleProcessor vehicleProcessor = new VehicleProcessor(new VehicleConverter(), new VehicleCombiner());
    List<Vehicle> vehicles = vehicleProcessor.process(sensorRecords);

    System.out.println("Records loaded...");

    // listen to commands
    VehicleStats stats = new VehicleStats();
    MenuCommand menuCommand = new MenuCommand();
    CountDayCommand countDayCommand = new CountDayCommand(stats);
    CountAverageCommand countAverageCommand = new CountAverageCommand(stats);
    PeakTimesCommand peakTimesCommand = new PeakTimesCommand(stats);
    SpeedCommand speedCommand = new SpeedCommand(stats);
    DistanceCommand distanceCommand = new DistanceCommand(stats);
    ExitCommand exitCommand = new ExitCommand();
    CommandLineListener commandLineListener = new CommandLineListener(vehicles);
    commandLineListener.addObserver(menuCommand);
    commandLineListener.addObserver(countDayCommand);
    commandLineListener.addObserver(countAverageCommand);
    commandLineListener.addObserver(peakTimesCommand);
    commandLineListener.addObserver(speedCommand);
    commandLineListener.addObserver(distanceCommand);
    commandLineListener.addObserver(exitCommand);
    new Thread(commandLineListener).start();

    Arrays.stream(CommandType.values()).forEach(commandType -> System.out.println(commandType.getDescription()));
  }

}
