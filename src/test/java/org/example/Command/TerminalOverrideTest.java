package org.example.Command;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


class TerminalOverrideTest {
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
    void override_pwd() {
        String[] args ={">","student"};
        terminal.pwd("pwd",args);  //
        String[] file ={"student"};
        terminal.cat(file);
        assertEquals(current.toString().trim(),outputStream.toString().trim());

    }

    @Test
    void override_pwd_Logical() {
        String[] args ={">","student"};
        terminal.pwd("pwd -L",args);  //
        String[] file ={"student"};
        terminal.cat(file);
        assertEquals(current.toString().trim(),outputStream.toString().trim());

    }

    @Test
    void override_pwd_Physical() {
        try{
            current=current.toRealPath();
        }
        catch (Exception e)
        {
            System.out.println("error with real path! "+e.getMessage());
        }
        String[] args ={">","student"};
        terminal.pwd("pwd -P",args);  //
        String[] file ={"student"};
        terminal.cat(file);
        assertEquals(current.toString().trim(),outputStream.toString().trim());

    }

    @Test
    void Override_pwd_root_path(){

        String[] root_path ={">","C:\\ziyad"};
        terminal.pwd("pwd",root_path);  //
        assertEquals("An error occurred while writing to the file: C:\\ziyad",outputStream.toString().trim());

    }

    @Test
    void Override_pwd_invalid_path(){

        String[] invalid_path={">","C:\\Users\\xyz\\ziyad"};
        terminal.pwd("pwd",invalid_path);
        assertEquals("Invalid path!",outputStream.toString().trim());

    }
}