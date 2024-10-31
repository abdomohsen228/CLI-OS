package org.example.Command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalMvTest {
    private Terminal terminal;
    private File testDirectory;
    private File sourceFile;
    private File destinationDirectory;

    @BeforeEach
    public void setUp() throws IOException {
        terminal = new Terminal();
        testDirectory = new File(System.getProperty("user.dir") + "/testDir");
        if (!testDirectory.exists()) {
            testDirectory.mkdir();
        }
        sourceFile = new File(testDirectory, "fileA.txt");
        sourceFile.createNewFile();

        destinationDirectory = new File(testDirectory, "destinationDir");
        destinationDirectory.mkdir();

        terminal.setCurrentDirectory(testDirectory);
    }

    @AfterEach
    public void tearDown() {
        // Clean up test directory after each test
        for (File file : testDirectory.listFiles()) {
            file.delete();
        }
        testDirectory.delete();
    }

    @Test
    public void testMvMove() {
        String sourcePath = "fileA.txt";
        String destinationPath = "destinationDir"; // Move to directory
        terminal.mv(sourcePath, destinationPath);

        // Check if the file has been moved
        File movedFile = new File(destinationDirectory, "fileA.txt");
        assertTrue(movedFile.exists(), "The file should exist in the destination directory.");

        // Check if the file no longer exists in the source directory
        assertFalse(sourceFile.exists(), "The source file should no longer exist.");
    }

    @Test
    public void testMvRename() {
        String sourcePath = "fileA.txt";
        String destinationPath = "destinationDir/fileB.txt"; // Move and rename
        terminal.mv(sourcePath, destinationPath);

        // Check if the file has been moved and renamed
        File movedFile = new File(destinationDirectory, "fileB.txt");
        assertTrue(movedFile.exists(), "The renamed file should exist in the destination directory.");

        // Check if the original file no longer exists
        assertFalse(sourceFile.exists(), "The source file should no longer exist.");
    }

    @Test
    public void testMoveFileToSameDirectory() {
        String sourcePath = "fileA.txt";
        String destinationPath = "fileA.txt"; // Move to the same name
        terminal.mv(sourcePath, destinationPath);

        // Check if the file still exists (no operation expected)
        assertTrue(sourceFile.exists(), "The file should still exist after moving to the same name.");
    }

}
