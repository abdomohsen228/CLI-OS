package org.example.Menu;

import org.example.Command.Terminal;
import org.example.parsing.Parser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Interface {
    private final Terminal terminal;
    private final ArrayList<String> commandHistory;
    private final Scanner scanner;

    public Interface() {
        this.terminal = new Terminal();
        this.commandHistory = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }
    public void chooseCommandAction() throws IOException {
        Parser parserObject = new Parser();
        String input;
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            if (input.equalsIgnoreCase("help")) {
                System.out.println("1. pwd  \n" +
                        "   - Prints the current working directory path.\n" +
                        "2. pwd -P  \n" +
                        "   - Prints the physical path of the current working directory, resolving any symbolic links.\n" +
                        "3. pwd -L  \n" +
                        "   - Prints the logical path of the current working directory, without resolving symbolic links.\n" +
                        "4. ls  \n" +
                        "   - Lists files and directories in the current directory.\n" +
                        "5. ls -a  \n" +
                        "   - Lists all files and directories.\n" +
                        "6. ls -r  \n" +
                        "   - Lists files and directories in reverse order.\n" +
                        "7. mkdir <directory_name>  \n" +
                        "   - Creates a new directory with the specified name.\n" +
                        "8. rmdir <directory_name>  \n" +
                        "   - Removes an empty directory with the specified name.\n" +
                        "9. touch <file_name>  \n" +
                        "   - Creates a new empty file or updates the timestamp of an existing file.\n" +
                        "10. mv <source> <destination>  \n" +
                        "    - Moves or renames a file or directory from the source path to the destination path.\n" +
                        "11. rm <file_name>  \n" +
                        "    - Removes (deletes) a file. Can also delete directories with the -r flag.\n" +
                        "12. cat <file_name>  \n" +
                        "    - Displays the content of a file.\n" +
                        "13. >  \n" +
                        "    - Redirects output of a command to a file, overwriting the file if it exists.\n" +
                        "14. >>  \n" +
                        "    - Redirects output of a command to a file, appending to the file if it exists.\n" +
                        "15. |  \n" +
                        "    - Pipes output of one command as input to another command, allowing chaining of commands.\n");
                continue;
            }
            parserObject.getArgs(input);
            if (parserObject.parse(parserObject.getCommandName())) {
                commandHistory.add(input);
                delegateToTerminal(parserObject, input);
            } else {
                System.out.println("Invalid Command!");
            }
        }
    }

    private void delegateToTerminal(Parser parserObject, String input) throws IOException {
        String commandName = parserObject.getCommandName();
        String[] args = parserObject.getArgs(input);
        switch (commandName) {
            case "mv":
                if (args.length == 2) {  // Ensure there are exactly 2 arguments for the mv command
                    terminal.mv(args[0], args[1]);
                } else {
                    System.out.println("Invalid arguments for mv command!");
                }
                break;

            case "pwd":
            case "pwd -L":
            case "pwd -P":
                terminal.pwd(commandName, args);
                break;
            case "cd":
                terminal.cd(args);
                break;
            case "ls":
            case "ls -r":
            case "ls -a":
                terminal.ls(commandName, args);
                break;
            case "touch":
                terminal.touch(args);
                break;
            case "history":
                terminal.history(commandHistory);
                break;
            case "mkdir":
                terminal.mkdir(args);
                break;
            case "rmdir":
                terminal.rmdir(args);
                break;
            case "cat":
                terminal.cat(args);
                break;
            case "wc":
//                terminal.wc(args);
                break;
            case "cp":
//                Terminal.cp(args);
                break;
            case "cp -r":
//                Terminal.cp_r(args);
                break;
            case "rm":
                terminal.rm(args);
                break;
            default:
                System.out.println("Invalid Command!");
        }
    }
}
