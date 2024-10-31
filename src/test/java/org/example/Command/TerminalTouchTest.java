package org.example.Command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalTouchTest {

    private Terminal terminal;
    private File testDirectory;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() throws IOException {
        terminal = new Terminal();
        testDirectory = new File(System.getProperty("user.dir") + "/testDir");
        if (!testDirectory.exists()) {
            testDirectory.mkdir();
        }
        terminal.setCurrentDirectory(testDirectory);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        deleteDirectoryRecursively(testDirectory);
        System.setOut(System.out);
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

    @Test
    public void testTouchWithAbsolutePath() {
        String absolutePath = testDirectory.getAbsolutePath() + "\\fileA.txt";
        terminal.touch(new String[]{absolutePath});
        File createdFile = new File(absolutePath);

        assertTrue(createdFile.exists(), "File should be created with absolute path.");
        assertTrue(outputStream.toString().contains("File created successfully!"));
    }

    @Test
    public void testTouchInCurrentDirectory() {
        String fileName = "fileC.txt";
        terminal.touch(new String[]{fileName});

        File createdFile = new File(testDirectory, fileName);
        assertTrue(createdFile.exists(), "File should be created in the current directory.");
        assertTrue(outputStream.toString().contains("File created successfully!"));
    }

    @Test
    public void testTouchWithInvalidArguments() {
        terminal.touch(new String[]{"invalid", "too", "many"});

        File[] files = testDirectory.listFiles();
        assertEquals(0, files.length, "No files should be created for invalid arguments.");
        assertTrue(outputStream.toString().contains("Invalid Command Arguments!"));
    }

    @Test
    public void testTouchForExistingFile() throws IOException {
        String fileName = "existingFile.txt";
        File file = new File(testDirectory, fileName);
        file.createNewFile();

        terminal.touch(new String[]{file.getAbsolutePath()});

        assertTrue(file.exists(), "Existing file should not be affected by touch.");
        assertTrue(outputStream.toString().contains("File already exists!"));
    }

    @Test
    public void testTouchInNewDirectory() {
        String relativePath = "newFolder\\newFile.txt";
        terminal.touch(new String[]{relativePath});

        File newFolder = new File(testDirectory, "newFolder");
        File newFile = new File(newFolder, "newFile.txt");

        assertTrue(newFolder.exists() && newFolder.isDirectory(), "New directory should be created.");
        assertTrue(newFile.exists(), "File should be created in the new directory.");
        assertTrue(outputStream.toString().contains("Directory created successfully!"));
        assertTrue(outputStream.toString().contains("File created successfully!"));
    }

    @Test
    public void testTouchInNonExistentAbsolutePath() {
        String invalidPath = "C:\\InvalidPath\\anotherFile.txt";
        terminal.touch(new String[]{invalidPath});

        assertFalse(new File(invalidPath).exists(), "File should not be created in a non-existent absolute path.");
        assertTrue(outputStream.toString().contains("Path doesn't exist or is not a directory!"));
    }
}
