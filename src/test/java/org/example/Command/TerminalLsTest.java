package org.example.Command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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

        terminal.setCurrentDirectory(testDirectory);
    }
    @Test
    public void testLsAlphabeticalOrder() {
        String[] input = new String[0];
        terminal.ls("ls", input);
        File[] expectedFiles = testDirectory.listFiles();
        Arrays.sort(expectedFiles);
        assertNotNull(expectedFiles);
        assertEquals("fileA.txt", expectedFiles[0].getName());
        assertEquals("fileB.txt", expectedFiles[1].getName());
        assertEquals("fileC.txt", expectedFiles[2].getName());
    }
    @Test
    public void testLsReverseOrder() {
        String[] input = new String[0];
        terminal.ls("ls -r", input);

        File[] expectedFiles = testDirectory.listFiles();
        Arrays.sort(expectedFiles);
        assertNotNull(expectedFiles);
        Arrays.sort(expectedFiles);
        assertEquals("fileC.txt", expectedFiles[2].getName());
        assertEquals("fileB.txt", expectedFiles[1].getName());
        assertEquals("fileA.txt", expectedFiles[0].getName());
    }
}
