package org.example.Command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalTouchTest {

    private Terminal terminal;
    private File testDirectory;

    @BeforeEach
    public void setUp() throws IOException {
        terminal = new Terminal();
        testDirectory = new File(System.getProperty("user.dir") + "/testDir");
        if (!testDirectory.exists()) {
            testDirectory.mkdir();
        }
        terminal.setCurrentDirectory(testDirectory);
    }

    @Test
    public void testTouchWithAbsolutePath() throws IOException {
        String absolutePath = testDirectory.getAbsolutePath() + "\\fileA.txt";
        terminal.touch(new String[]{absolutePath});
        File createdFile = new File(absolutePath);
        assertTrue(createdFile.exists(), "File should be created with absolute path.");
    }


    @Test
    public void testTouchInCurrentDirectory() throws IOException {
        String fileName = "fileC.txt";
        terminal.touch(new String[]{fileName});

        File createdFile = new File(testDirectory, fileName);
        assertTrue(createdFile.exists(), "File should be created in the current directory.");
    }

    @Test
    public void testTouchWithInvalidArguments() {
        terminal.touch(new String[]{"invalid", "too", "many"});

        File[] files = testDirectory.listFiles();
        assertEquals(0, files.length, "No files should be created for invalid arguments.");
    }

    @Test
    public void testTouchForExistingFile() throws IOException {
        String fileName = "existingFile.txt";
        File file = new File(testDirectory, fileName);
        file.createNewFile();

        terminal.touch(new String[]{file.getAbsolutePath()});

        assertTrue(file.exists(), "Existing file should not be affected by touch.");
    }
    @AfterEach
    public void tearDown() {
        deleteDirectoryRecursively(testDirectory);
    }
    private void deleteDirectoryRecursively(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryRecursively(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

}
