package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.services.GenreService;

import java.sql.SQLException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class AdminCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Start H2 console", key = "console")
    public void startConsole() throws SQLException {
        Console.main();
    }
}
