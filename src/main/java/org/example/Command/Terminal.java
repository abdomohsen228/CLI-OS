package org.example.Command;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Terminal {
    File currentDirectory = new File(System.getProperty("user.dir"));

    public void setCurrentDirectory(File directory) {
        this.currentDirectory = directory;
    }

    public File getCurrentDirectory() {
        return this.currentDirectory;
    }


    // Command 1: lists the contents of the current directory
    // sorted in alphabetical order or reversed
    public void ls(String command, String[] input) {
        File[] files = currentDirectory.listFiles();
        // check if the directory is empty
        if (files == null) {
            System.out.println("Directory is Empty!");
            return;
        }

        // Handle "ls" without any options (only visible files)
        if (command.equals("ls") && input.length == 0) {
            Arrays.sort(files);
            for (File file : files) {
                // Exclude hidden files by checking if the file name starts with a dot
                if (!file.getName().startsWith(".") && !file.isHidden()) {
                    System.out.println(file.getName());
                }
            }
        }

        // Handle "ls -r" (only visible files in reverse order)
        else if (command.equals("ls -r") && input.length == 0) {
            Arrays.sort(files);
            Collections.reverse(Arrays.asList(files));
            for (File file : files) {
                // Exclude hidden files by checking if the file name starts with a dot
                if (!file.getName().startsWith(".") && !file.isHidden()) {
                    System.out.println(file.getName());
                }
            }
        }

        // Handle "ls -a" (all files, including hidden ones)
        else if (command.equals("ls -a") && input.length == 0) {
            Arrays.sort(files);
            for (File file : files) {
                System.out.println(file.getName());  // Include hidden files
            }
        }

        // If the command doesn't match any known pattern
        else {
            System.out.println("Invalid Command Arguments!");
        }
    }
    // Command 2: changes the current directory
    public void cd(String[] args) {
        if (args.length == 0) {
            String userHome = System.getProperty("user.home");
            if (currentDirectory.getAbsolutePath().equals(userHome)) {
                System.out.println("You are already in the home directory!");
            } else {
                System.setProperty("user.dir", userHome);
                currentDirectory = new File(userHome);
                System.out.println("Current Directory: " + currentDirectory.getAbsolutePath());
            }
        }

        else if (args.length == 1 && args[0].equals("..")) {
            File parentDirectory = currentDirectory.getParentFile();
            if (parentDirectory != null) {
                System.setProperty("user.dir", parentDirectory.getAbsolutePath());
                currentDirectory = parentDirectory;
                System.out.println("Current Directory: " + currentDirectory.getAbsolutePath());
            } else {
                System.out.println("You are already in the root directory!");
            }
        }

        else if (args.length == 1) {
            File targetDirectory = args[0].contains(":") ? new File(args[0]) : new File(currentDirectory, args[0]);
            if (!targetDirectory.exists()) {
                System.out.println("Path doesn't exist!");
            } else if (!targetDirectory.isDirectory()) {
                System.out.println("The specified path is not a directory!");
            } else {
                System.setProperty("user.dir", targetDirectory.getAbsolutePath());
                currentDirectory = targetDirectory;
                System.out.println("Current Directory: " + currentDirectory.getAbsolutePath());
            }
        }
        else {
            System.out.println("Invalid Command Arguments!");
        }
    }
    // Command 3: creates a new file in a given path
    public void touch(String[] input) {
        if (input.length != 1) {
            System.out.println("Invalid Command Arguments!");
            return;
        }
        File file;
        if (input[0].contains(":")) {
            String fileName = input[0].substring(input[0].lastIndexOf("\\") + 1);
            String filePath = input[0].substring(0, input[0].lastIndexOf("\\"));
            File directory = new File(filePath);

            if (!directory.exists() || !directory.isDirectory()) {
                System.out.println("Path doesn't exist or is not a directory!");
                return;
            }

            file = new File(directory, fileName);
        }

        else if (input[0].contains("\\")) {
            file = new File(currentDirectory, input[0]);
            File parentDirectory = file.getParentFile();

            if (parentDirectory != null && !parentDirectory.exists()) {
                boolean created = parentDirectory.mkdirs();
                if (created) {
                    System.out.println("Directory created successfully!");
                } else {
                    System.out.println("Failed to create the directory!");
                    return;
                }
            }
        }
        else {
            file = new File(currentDirectory, input[0]);
        }
        try {
            if (file.exists()) {
                System.out.println("File already exists!");
            } else {
                boolean isCreated = file.createNewFile();
                System.out.println(isCreated ? "File created successfully!" : "File already exists!");
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    // Command 4: list the history of the commands
    public void history(ArrayList<String> arr) {
        for (int i = 0; i < arr.size(); i++) {
            System.out.println((i + 1) + "  " + arr.get(i));
        }
    }
     // Command: mkdir - creates directories
    public void mkdir(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: mkdir <directory_name>");
            return;
        }
        for (String dirName : args) {
            File dir = new File(currentDirectory, dirName);
            if (dir.exists()) {
                System.out.println("Directory " + dirName + " already exists!");
            } else if (dir.mkdirs()) {
                System.out.println("Directory " + dirName + " created successfully.");
            } else {
                System.out.println("Failed to create directory " + dirName + ".");
            }
        }
    }
    // Command: rmdir - removes empty directories
    public void rmdir(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: rmdir <directory_name>");
            return;
        }
        for (String dirName : args) {
            File dir = new File(currentDirectory, dirName);
            if (!dir.exists()) {
                System.out.println("Directory " + dirName + " does not exist!");
            } else if (!dir.isDirectory()) {
                System.out.println(dirName + " is not a directory!");
            } else if (dir.listFiles() != null && dir.listFiles().length > 0) {
                System.out.println("Directory " + dirName + " is not empty!");
            } else if (dir.delete()) {
                System.out.println("Directory " + dirName + " deleted successfully.");
            } else {
                System.out.println("Failed to delete directory " + dirName + ".");
            }
        }
    }
    // Command: rm - removes files
    public void rm(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: rm <file_name>");
            return;
        }
        for (String fileName : args) {
            File file = new File(currentDirectory, fileName);
            if (!file.exists()) {
                System.out.println("File " + fileName + " does not exist!");
            } else if (!file.isFile()) {
                System.out.println(fileName + " is not a file!");
            } else if (file.delete()) {
                System.out.println("File " + fileName + " deleted successfully.");
            } else {
                System.out.println("Failed to delete file " + fileName + ".");
            }
        }
    }
    // Command: cat - displays or creates files
    public void cat(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: cat <file_name> [> or >> output_file]");
            return;
        }

        // Check for redirection operators ">" or ">>"
        boolean append = false;
        if (args.length > 1 && (args[1].equals(">") || args[1].equals(">>"))) {
            append = args[1].equals(">>"); // Set to true if '>>' is used
            if (args.length < 3) {
                System.out.println("Usage for redirection: cat <file_name> > <output_file>");
                return;
            }
            // Attempt to open source file and write to destination with specified mode
            try (Scanner scanner = new Scanner(new File(currentDirectory, args[0]));
                 FileWriter writer = new FileWriter(new File(currentDirectory, args[2]), append)) {
                while (scanner.hasNextLine()) {
                    writer.write(scanner.nextLine() + System.lineSeparator());
                }
                System.out.println("File contents successfully " + (append ? "appended to " : "redirected to ") + args[2]);
            } catch (FileNotFoundException e) {
                System.out.println("File " + args[0] + " not found.");
            } catch (IOException e) {
                System.out.println("Error writing to " + args[2]);
            }
        } else {
            // If no redirection, read and display file contents
            for (String fileName : args) {
                File file = new File(currentDirectory, fileName);
                if (!file.exists()) {
                    System.out.println("File " + fileName + " does not exist!");
                    continue;
                }
                if (!file.isFile()) {
                    System.out.println(fileName + " is not a file!");
                    continue;
                }
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        System.out.println(scanner.nextLine());
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error reading file " + fileName + ".");
                }
            }
        }
    }
    // Command: pwd - displays current working directory
    public void pwd(String command, String[] args) {
        Path current = Paths.get("").toAbsolutePath();
//        System.out.println("args are: "+Arrays.toString(args)+"\n");

        // Check for invalid command
        if (!(command.equals("pwd") || command.equals("pwd -P") || command.equals("pwd -L"))) {
            System.out.println("Invalid syntax!");
            return ;
        }
        if(command.equals("pwd -P"))
        {
            try{
                current=current.toRealPath();

            }
            catch (Exception e)
            {
                System.out.println("error with real path! "+e.getMessage());
                return ;
            }
        }

        // Handle options and output redirection
        if (args.length > 0) {
            if (args[0].equals(">")) {
                Override(current.toString(), args[1]);
                return ;
            } else if (args[0].equals(">>")) {
                Append(current.toString(), args[1]);
                return ;
            } else {
                System.out.println("Invalid syntax!");
                return ;
            }
        }

        // No arguments
        System.out.println(current);

    }
    // command: > - overrides file's content and creates new one if doesn't exist
    public void Override(String content, String outputFile) {
        Path file = Paths.get(outputFile);


        // file at first level or the parent is invalid
        if (file.getParent() != null && !Files.exists(file.getParent())) {
            System.out.println("Invalid path!");
            return;
        }

        // Write to the file  // will fail if directory to directory
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    // command: >> - appends the current content to a new file
    public void Append(String content , String outputFile )
    {
        Path file = Paths.get(outputFile);

        // Check if the parent directory exists
        if (file.getParent() != null && !Files.exists(file.getParent())) {
            System.out.println("Invalid path!");
            return;
        }

        // Append to the file
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    public void mv(String sourcePath, String destinationPath) {
        File sourceFile = new File(currentDirectory, sourcePath);
        File destinationFile = new File(currentDirectory, destinationPath);

        // Check if source file exists
        if (!sourceFile.exists()) {
            System.out.println("Source file or directory does not exist!");
            return;
        }

        // Check if the destination is a directory; if so, move the file into the directory.
        if (destinationFile.isDirectory()) {
            destinationFile = new File(destinationFile, sourceFile.getName());
        } else if (!destinationFile.exists()) {
            // If the destination does not exist and is not a directory, rename the file.
            destinationFile = new File(destinationPath);
        }


        // Move or rename the file
        try {
            Files.move(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved/Renamed " + sourceFile.getName() + " to " + destinationFile.getPath());
        } catch (IOException e) {
            System.out.println("Error moving/renaming file: " + e.getMessage());
        }
    }


}

