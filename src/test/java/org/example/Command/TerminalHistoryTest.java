package org.example.Command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalHistoryTest {

    private Terminal terminal;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        terminal = new Terminal();
        System.setOut(new PrintStream(outContent));
    }
    @Test
    public void testHistoryWithMultipleCommands() {
        ArrayList<String> history = new ArrayList<>();
        history.add("cd ..");
        history.add("ls");
        history.add("mkdir test");
        terminal.history(history);

        String expectedOutput = "1  cd .." + System.lineSeparator() +
                "2  ls" + System.lineSeparator() +
                "3  mkdir test" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString(), "History output should match expected format.");
    }

    @Test
    public void testHistoryWithNoCommands() {
        ArrayList<String> history = new ArrayList<>();
        terminal.history(history);
        String expectedOutput = "";
        assertEquals(expectedOutput, outContent.toString(), "No commands should result in no output.");
    }

    @Test
    public void testHistoryWithSingleCommand() {
        ArrayList<String> history = new ArrayList<>();
        history.add("touch file.txt");
        terminal.history(history);
        String expectedOutput = "1  touch file.txt" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString(), "History with one command should match expected format.");
    }
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }
}
