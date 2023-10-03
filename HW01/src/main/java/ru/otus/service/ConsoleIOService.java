package ru.otus.service;

public class ConsoleIOService implements IOService {
    @Override
    public void println(String string) {
        System.out.println(string);
    }
}
