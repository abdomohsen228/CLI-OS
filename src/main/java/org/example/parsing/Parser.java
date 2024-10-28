package org.example.parsing;

import java.util.Arrays;
import java.util.Map;

public class Parser {
    private String commandName;
    private String[] args;

    // check if the command is valid
    public boolean parse(String command) {
        Map<String, Boolean> validCommands = Map.ofEntries(
                Map.entry("ls", true),
                Map.entry("ls -r", true),
                Map.entry("cd", true),
                Map.entry("touch", true),
                Map.entry("history", true),
                Map.entry("pwd", true),
                Map.entry("pwd -L", true),
                Map.entry("pwd -P", true),
                Map.entry("mkdir", true),
                Map.entry("rmdir", true),
                Map.entry("cp", true),
                Map.entry("cp -r", true),
                Map.entry("rm", true),
                Map.entry("cat", true),
                Map.entry("wc", true)
        );
        return validCommands.containsKey(command);
    }

    // parse the input to --> command + list of arguments
    public String[] getArgs(String input) {
        // remove leading and trailing spaces
        input = input.trim();
        // split the input by spaces
        args = input.split("\\s+");
        commandName = args[0];

        // handling "cp -r" and "ls -r" command
        if ((commandName.equals("cp") || commandName.equals("ls")) && args.length >= 2 && args[1].equals("-r")) {
            commandName += " -r";
            // remove the first two arguments from the list
            args = Arrays.copyOfRange(args, 2, args.length);
        } else if (commandName.equals("pwd") && args.length >= 2 && (args[1].equals("-L")||args[1].equals("-P"))) {
            commandName += " ";
            commandName += args[1];
            // remove the first two arguments from the list
            args = Arrays.copyOfRange(args, 2, args.length);
        } else {
            // remove the first argument from the list
            args = Arrays.copyOfRange(args, 1, args.length);
        }
        return args;
    }
    public String getCommandName() {
        return commandName;
    }
}