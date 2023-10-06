package ru.otus.service;

import java.io.PrintStream;

public class StreamIOService implements IOService {

    private final PrintStream printStream  ;

    public StreamIOService(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void println(String prompt) {
        printStream.println(prompt);
    }
}
