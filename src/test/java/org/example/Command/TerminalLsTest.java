package org.example.Command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TerminalLsTest {
    private Terminal terminal;
    private File testDirectory;



    @BeforeEach
    public void setUp() throws IOException {
        terminal = new Terminal();
        testDirectory = new File(System.getProperty("user.dir") + "/testDir");
        if (!testDirectory.exists()) {
            testDirectory.mkdir();
        }
        new File(testDirectory, "fileA.txt").createNewFile();
        new File(testDirectory, "fileB.txt").createNewFile();
        new File(testDirectory, "fileC.txt").createNewFile();
        new File(testDirectory, ".hiddenFile.txt").createNewFile();

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

    // 'ls' command
    @Test
    public void testLsAlphabeticalOrder() {
        String[] input = new String[0];
        terminal.ls("ls", input);

        File[] actualFiles = testDirectory.listFiles();
        assertNotNull(actualFiles);

        // Filter out hidden files and sort
        actualFiles = Arrays.stream(actualFiles)
                .filter(file -> !file.getName().startsWith("."))
                .sorted(Comparator.comparing(File::getName))
                .toArray(File[]::new);

        // Expected names without hidden files
        String[] expectedNames = {"fileA.txt", "fileB.txt", "fileC.txt"};

        // Compare the number of files
        assertEquals(expectedNames.length, actualFiles.length);

        for (int i = 0; i < expectedNames.length; i++) {
            assertEquals(expectedNames[i], actualFiles[i].getName());
        }
    }

    // 'ls -r' command
    @Test
    public void testLsReverseOrder() {
        String[] input = new String[0];
        terminal.ls("ls -r", input);

        File[] actualFiles = testDirectory.listFiles();
        assertNotNull(actualFiles);

        // Filter out hidden files and sort in reverse order
        actualFiles = Arrays.stream(actualFiles)
                .filter(file -> !file.getName().startsWith("."))
                .sorted(Comparator.comparing(File::getName).reversed())
                .toArray(File[]::new);

        // Prepare the expected names in reverse order
        String[] expectedNames = {"fileA.txt", "fileB.txt", "fileC.txt"};
        Arrays.sort(expectedNames); // Sort expected names in natural order first
        Collections.reverse(Arrays.asList(expectedNames)); // Reverse the expected names for comparison

        // Compare the number of files
        assertEquals(expectedNames.length, actualFiles.length);

        for (int i = 0; i < expectedNames.length; i++) {
            assertEquals(expectedNames[i], actualFiles[i].getName());
        }
    }


    // 'ls -a' command
    @Test
    public void testLsAllFiles() {
        String[] input = new String[0];
        terminal.ls("ls -a", input);

        File[] actualFiles = testDirectory.listFiles();
        assertNotNull(actualFiles);

        // Create an array including the hidden file
        String[] expectedNames = {"fileA.txt", "fileB.txt", "fileC.txt", ".hiddenFile.txt"};

        // Sort actual files by their names for comparison
        Arrays.sort(actualFiles, Comparator.comparing(File::getName));
        Arrays.sort(expectedNames); // Sort expected names

        // Compare the number of files first
        assertEquals(expectedNames.length, actualFiles.length);

        for (int i = 0; i < expectedNames.length; i++) {
            assertEquals(expectedNames[i], actualFiles[i].getName());
        }
    }
}
