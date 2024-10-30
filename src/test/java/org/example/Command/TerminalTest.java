package org.example.Command;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.nio.file.Paths;


import static org.junit.jupiter.api.Assertions.*;

class TerminalTest {

    Terminal terminal;

    @BeforeEach
    void setup(){
        terminal = new Terminal();
    }
    @Test
    void pwd_physical() {
        String current = Paths.get("").toAbsolutePath().toString();
        try {
            current = Paths.get("").toRealPath().toString();
        }
        catch(Exception e)
        {

        }
        String[] args={};
        assertEquals(current,terminal.pwd("pwd -P",args));

    }
    @Test
    void pwd_logical() {
        String current = Paths.get("").toAbsolutePath().toString();
        String[] args={};
        assertEquals(current,terminal.pwd("pwd -L",args));
    }
    @Test
    void pwd() {
        String current = Paths.get("").toAbsolutePath().toString();
        String[] args={};
        assertEquals(current,terminal.pwd("pwd",args));
    }

    @Test
    void override() {
    }

    @Test
    void append() {
    }
}