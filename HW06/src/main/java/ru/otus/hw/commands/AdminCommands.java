package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import java.sql.SQLException;

@RequiredArgsConstructor
@ShellComponent
public class AdminCommands {

    @ShellMethod(value = "Start H2 console", key = "console")
    public void startConsole() throws SQLException {
        Console.main();
    }
}
