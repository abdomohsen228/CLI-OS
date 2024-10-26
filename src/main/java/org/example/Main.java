package org.example;
import org.example.Menu.Interface;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Interface userInterface = new Interface();
        userInterface.chooseCommandAction();
    }
}