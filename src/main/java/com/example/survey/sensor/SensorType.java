package com.example.survey.sensor;

public enum SensorType {

  A("northbound"), B("southbound");

  private String direction;

  SensorType(final String direction) {
    this.direction = direction;
  }

  public String getDirection() {
    return direction;
  }

}
