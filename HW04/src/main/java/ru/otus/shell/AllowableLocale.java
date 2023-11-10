package ru.otus.shell;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AllowableLocale {
    ru ("ru-RU"),
    en ("en-US");

   private String value;
}
