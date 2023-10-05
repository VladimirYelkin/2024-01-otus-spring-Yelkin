package ru.otus.service;

import java.io.PrintStream;

public class ConsoleIOService implements IOService {

    private final PrintStream printStream  ;

    public ConsoleIOService(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void println(String string) {
        printStream.println(string);
    }
}
