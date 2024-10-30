package org.example.Command;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import static org.junit.jupiter.api.Assertions.*;


class TerminalpwdTest {

    Terminal terminal;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private Path current = Paths.get("").toAbsolutePath();

    @BeforeEach
    void setup()
    {
        terminal = new Terminal();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void fix()
    {
        System.setOut(originalOut);
    }

    @Test
    void pwd() {
        String[] args={};
        terminal.pwd("pwd",args);
        assertEquals(current.toString().trim(),outputStream.toString().trim());

    }
    @Test
    void pwd_logical() {
        String[] args={};
        terminal.pwd("pwd -L",args);
        assertEquals(current.toString().trim(),outputStream.toString().trim());

    }

    @Test
    void pwd_physical() {
        try{
            current=current.toRealPath();
        }
        catch (Exception e)
        {
            System.out.println("error with real path! "+e.getMessage());
        }
        String[] args={};
        terminal.pwd("pwd -P",args);
        assertEquals(current.toString().trim(),outputStream.toString().trim());

    }
    @Test
    void pwd_with_data() {
        String[] args={"data"};
        terminal.pwd("pwd",args);
        assertEquals("Invalid syntax!",outputStream.toString().trim());
    }
    @Test
    void pwd_Logical_with_data() {
        String[] args={"data"};
        terminal.pwd("pwd -L",args);
        assertEquals("Invalid syntax!",outputStream.toString().trim());
    }
    @Test
    void pwd_Physical_with_data() {
        try{
            current=current.toRealPath();
        }
        catch (Exception e)
        {
            System.out.println("error with real path! "+e.getMessage());
        }
        String[] args={"data"};
        terminal.pwd("pwd -P",args);
        assertEquals("Invalid syntax!",outputStream.toString().trim());
    }


}