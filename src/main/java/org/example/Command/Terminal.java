package org.example.Command;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
public class Terminal {
    File currentDirectory = new File(System.getProperty("user.dir"));

    // Command 1: lists the contents of the current directory
    // sorted in alphabetical order or reversed
    public void ls(String command, String[] input) {
        File[] files = currentDirectory.listFiles();
        // check if the directory is empty
        if (files == null) {
            System.out.println("Directory is Empty!");
            return;
        }
        // sort the files in alphabetical order
        if (command.equals("ls") && input.length == 0) {
            Arrays.sort(files);
            for (File file : files) {
                System.out.println(file.getName());
            }
        }
        // sort the files in reversed order
        else if (command.equals("ls -r") && input.length == 0) {
            Arrays.sort(files);
            Collections.reverse(Arrays.asList(files));
            for (File file : files) {
                System.out.println(file.getName());
            }
        } else {
            System.out.println("Invalid Command Arguments!");
        }
    }

    // Command 2: changes the current directory
    public void cd(String[] args) {
        // cd to home directory
        if (args.length == 0) {
            // check if the user is in the home directory
            if (currentDirectory.getAbsolutePath().equals(System.getProperty("user.home"))) {
                System.out.println("You are already in the home directory!");
            } else {
                // Change the current directory to the user's home directory
                String userHome = System.getProperty("user.home");
                System.setProperty("user.dir", userHome);
                currentDirectory = new File(userHome);
                System.out.println("Current Directory: " + currentDirectory.getAbsolutePath());
            }
        }

        // cd to parent directory
        else if (args[0].equals("..") && args.length == 1) {
            // Change the current directory to the parent directory
            File parentDirectory = currentDirectory.getParentFile();
            // check that we don't stand at the root directory
            if (parentDirectory != null) {
                System.setProperty("user.dir", parentDirectory.getAbsolutePath());
                currentDirectory = parentDirectory;
            } else {
                System.out.println("You are already in the root directory!");
            }
            System.out.println("Current Directory: " + currentDirectory.getAbsolutePath());
        }

        // cd to relative or full path
        else if (args.length == 1) {
            // check if the path exists
            File file = new File(args[0]);
            if (!file.exists()) {
                System.out.println("Path doesn't exits!");
            } else {
                // Change the current directory to the given path
                System.setProperty("user.dir", args[0]);
                currentDirectory = new File(System.getProperty("user.dir"));
                System.out.println("Current Directory: " + currentDirectory.getAbsolutePath());
            }
        }
        // another invalid command argument
        else {
            System.out.println("Invalid Command Arguments!");
        }
    }
    // Command 3: creates a new file in a given path
    public void touch(String[] input) {
        // touch create absolute path
        if (input.length == 1 && input[0].contains(":")) {
            // parse the path into the file name and the absolute path
            String fileName = input[0].substring(input[0].lastIndexOf("\\") + 1);
            String filePath = input[0].substring(0, input[0].lastIndexOf("\\"));
            // check if the path exists
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Path doesn't exits!");
            } else {
                // add the file to the given path
                file = new File(filePath, fileName);
                try {
                    // check if the file is already created
                    boolean isCreated = file.createNewFile();
                    if (isCreated) {
                        System.out.println("File created successfully!");
                    } else {
                        System.out.println("File already exists!");
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred!");
                }
            }
        }
        // touch create relative path
        else if (input.length == 1 && input[0].contains("\\")) {
            String relativePath = input[0];
            File file = new File(relativePath);
            File parentDirectory = file.getParentFile();
            // check if the directory exists
            if (!parentDirectory.exists()) {
                boolean created = parentDirectory.mkdirs();
                if (created) {
                    System.out.println("Directory created successfully!");
                } else {
                    System.out.println("Error occurred!");
                }
            }
            try {
                // check if the file is already created
                boolean isCreated = file.createNewFile();
                if (isCreated) {
                    System.out.println("File created successfully!");
                } else {
                    System.out.println("File already exists!");
                }
            } catch (IOException e) {
                System.out.println("Error occurred!");
            }
        }
        // touch create file in the current directory
        else if (input.length == 1 && !input[0].contains("\\")) {
            String fileName = input[0];
            // create the file in the current directory
            File file = new File(currentDirectory, fileName);
            try {
                // check if the file is already created
                boolean isCreated = file.createNewFile();
                if (isCreated) {
                    System.out.println("File created successfully!");
                } else {
                    System.out.println("File already exists!");
                }
            } catch (IOException e) {
                System.out.println("Error occurred!");
            }
        }
        // Handle invalid command arguments
        else {
            System.out.println("Invalid Command Arguments!");
        }
    }
    // Command 4: list the history of the commands
    public void history(ArrayList<String> arr) {
        for (int i = 0; i < arr.size(); i++) {
            System.out.println((i + 1) + "  " + arr.get(i));
        }
    }
}
