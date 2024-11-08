package org.example.Command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalCdTest {
    private Terminal terminal;
    private File testDirectory;
    private File homeDirectory;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() throws IOException {
        terminal = new Terminal();
        testDirectory = new File(System.getProperty("user.dir") + "/testDir");
        if (!testDirectory.exists()) {
            testDirectory.mkdir();
        }
        homeDirectory = new File(System.getProperty("user.home"));
        terminal.setCurrentDirectory(testDirectory);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        if (testDirectory.exists()) {
            deleteDirectory(testDirectory);
        }
        System.setOut(System.out);
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    @Test
    public void testCdToHomeDirectory() {
        assertEquals(testDirectory.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
        terminal.cd(new String[0]);
        assertEquals(homeDirectory.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
        assertTrue(outputStream.toString().contains("Current Directory: " + homeDirectory.getAbsolutePath()));
    }

    @Test
    public void testCdToParentDirectory() {
        assertEquals(testDirectory.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
        terminal.cd(new String[]{".."});
        assertEquals(testDirectory.getParentFile().getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
        assertTrue(outputStream.toString().contains("Current Directory: " + testDirectory.getParentFile().getAbsolutePath()));
    }

    @Test
    public void testCdToNonExistentDirectory() {
        terminal.cd(new String[]{"nonExistentDir"});
        assertEquals(testDirectory.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
        assertTrue(outputStream.toString().contains("Path doesn't exist!"));
    }

    @Test
    public void testCdToValidDirectory() throws IOException {
        File newDir = new File(testDirectory, "newDir");
        if (!newDir.exists()) {
            newDir.mkdir();
        }
        terminal.cd(new String[]{newDir.getAbsolutePath()});
        assertEquals(newDir.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
        assertTrue(outputStream.toString().contains("Current Directory: " + newDir.getAbsolutePath()));
    }

    @Test
    public void testCdWithInvalidArguments() {
        terminal.cd(new String[]{"invalid", "too", "many"});
        assertEquals(testDirectory.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
        assertTrue(outputStream.toString().contains("Invalid Command Arguments!"));
    }
}
