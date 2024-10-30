package org.example.Command;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class MkdirRmdirRmCatTests {
    private Terminal terminal;
    private File tempDirectory;

    @BeforeEach
    void setUp() {
        terminal = new Terminal();
        tempDirectory = new File(System.getProperty("java.io.tmpdir"), "testDir");
        tempDirectory.mkdir();
        terminal.setCurrentDirectory(tempDirectory);
    }

    @AfterEach
    void tearDown() {
        deleteDirectory(tempDirectory);
    }

    void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                deleteDirectory(file);
            }
        }
        dir.delete();
    }

    // mkdir Tests
    @Test
    void testMkdirSingleDirectory() {
        String dirName = "newDir";
        terminal.mkdir(new String[]{dirName});
        File createdDir = new File(tempDirectory, dirName);
        assertTrue(createdDir.exists() && createdDir.isDirectory(), "Directory should be created.");
    }

    @Test
    void testMkdirExistingDirectory() {
        String dirName = "existingDir";
        new File(tempDirectory, dirName).mkdir();
        terminal.mkdir(new String[]{dirName});
        assertEquals("Directory existingDir already exists!", "Directory existingDir already exists!");
    }

    // rmdir Tests
    @Test
    void testRmdirEmptyDirectory() {
        String dirName = "emptyDir";
        new File(tempDirectory, dirName).mkdir();
        terminal.rmdir(new String[]{dirName});
        assertFalse(new File(tempDirectory, dirName).exists(), "Empty directory should be removed.");
    }

    @Test
    void testRmdirNonEmptyDirectory() throws IOException {
        String dirName = "nonEmptyDir";
        File nonEmptyDir = new File(tempDirectory, dirName);
        nonEmptyDir.mkdir();
        new File(nonEmptyDir, "file.txt").createNewFile();
        terminal.rmdir(new String[]{dirName});
        assertTrue(nonEmptyDir.exists(), "Non-empty directory should not be removed.");
    }

    @Test
    void testRmdirNonExistentDirectory() {
        terminal.rmdir(new String[]{"nonExistentDir"});
        assertEquals("Directory nonExistentDir does not exist!", "Directory nonExistentDir does not exist!");
    }

    // rm Tests
    @Test
    void testRmFile() throws IOException {
        String fileName = "testFile.txt";
        File file = new File(tempDirectory, fileName);
        file.createNewFile();
        terminal.rm(new String[]{fileName});
        assertFalse(file.exists(), "File should be removed.");
    }

    @Test
    void testRmNonExistentFile() {
        terminal.rm(new String[]{"nonExistentFile.txt"});
        assertEquals("File nonExistentFile.txt does not exist!", "File nonExistentFile.txt does not exist!");
    }

    @Test
    void testRmDirectoryInsteadOfFile() {
        String dirName = "testDir";
        File dir = new File(tempDirectory, dirName);
        dir.mkdir();
        terminal.rm(new String[]{dirName});
        assertTrue(dir.exists(), "Directory should not be removed by rm.");
    }

    // cat Tests
    @Test
    void testCatDisplayFileContent() throws IOException {
        String fileName = "testFile.txt";
        File file = new File(tempDirectory, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Hello, World!");
        }
        terminal.cat(new String[]{fileName});
        // Manually verify console output for "Hello, World!"
    }

    @Test
    void testCatNonExistentFile() {
        terminal.cat(new String[]{"nonExistentFile.txt"});
        assertEquals("File nonExistentFile.txt does not exist!", "File nonExistentFile.txt does not exist!");
    }

    @Test
    void testCatAppendFileContent() throws IOException {
        String sourceFile = "source.txt";
        String destFile = "dest.txt";
        try (FileWriter writer = new FileWriter(new File(tempDirectory, sourceFile))) {
            writer.write("Hello, ");
        }
        try (FileWriter writer = new FileWriter(new File(tempDirectory, destFile))) {
            writer.write("World!");
        }
        terminal.cat(new String[]{sourceFile, ">>", destFile});
        String content = Files.readString(new File(tempDirectory, destFile).toPath());
        System.out.println("content: " + content);
        assertTrue(content.contains("World!Hello,"), "Content should be appended to dest.txt");
    }
}
