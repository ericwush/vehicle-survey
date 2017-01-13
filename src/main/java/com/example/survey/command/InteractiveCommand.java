package com.example.survey.command;

public interface InteractiveCommand extends Command {

  default void updateListenerAndExecute(final CommandLineListener listener) {
    listener.setListenNewCommand(false);
    System.out.println(getType().getDescription());
    try {
      execute(listener);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println("Back to top level menu");
    }
    listener.setListenNewCommand(true);
  }

  void execute(CommandLineListener listener);

}
