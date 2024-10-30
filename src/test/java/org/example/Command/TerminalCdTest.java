package org.example.Command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalCdTest {
    private Terminal terminal;
    private File testDirectory;
    private File homeDirectory;

    @BeforeEach
    public void setUp() throws IOException {
        terminal = new Terminal();
        testDirectory = new File(System.getProperty("user.dir") + "/testDir");
        if (!testDirectory.exists()) {
            testDirectory.mkdir();
        }
        homeDirectory = new File(System.getProperty("user.home"));
        terminal.setCurrentDirectory(testDirectory);
    }
    @Test
    public void testCdToHomeDirectory() {
        assertEquals(testDirectory.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
        terminal.cd(new String[0]);
        assertEquals(homeDirectory.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
    }

    @Test
    public void testCdToParentDirectory() {
        assertEquals(testDirectory.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
        terminal.cd(new String[]{".."});
        assertEquals(testDirectory.getParentFile().getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
    }

    @Test
    public void testCdToNonExistentDirectory() {
        terminal.cd(new String[]{"nonExistentDir"});
        assertEquals(testDirectory.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
    }

    @Test
    public void testCdToValidDirectory() throws IOException {
        File newDir = new File(testDirectory, "newDir");
        if (!newDir.exists()) {
            newDir.mkdir();
        }
        terminal.cd(new String[]{newDir.getAbsolutePath()});
        assertEquals(newDir.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
    }

    @Test
    public void testCdWithInvalidArguments() {
        terminal.cd(new String[]{"invalid", "too", "many"});
        assertEquals(testDirectory.getAbsolutePath(), terminal.getCurrentDirectory().getAbsolutePath());
    }
}
