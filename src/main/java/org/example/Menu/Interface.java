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
            case "echo":
//                terminal.echo(args);
                break;
            case "pwd":
//                System.out.println(Terminal.pwd(args));
                break;
            case "cd":
                terminal.cd(args);
                break;
            case "ls":
            case "ls -r":
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
