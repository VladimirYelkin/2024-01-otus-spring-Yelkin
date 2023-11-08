package ru.otus.service.localize;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}
