package ru.otus.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.config.LocaleConfig;
import ru.otus.service.TestRunnerService;
import ru.otus.service.localize.LocalizedIOService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationEventsCommands {

    private final LocalizedIOService localizedIOService;

    private final TestRunnerService testRunnerService;

    private final LocaleConfig localeConfig;

    @ShellMethod(value = "Set locale ", key = {"l","locale"})
    public String locale(@ShellOption(defaultValue = "ru", help = "locale [ru,en]") AllowableLocale locale) {
        localeConfig.setLocale(locale.getValue().toString());
        return localizedIOService.getMessage("Shell.info.prompt", localeConfig.getLocale());
    }

    @ShellMethod(value = "Start test", key = {"s", "start"})
    public String startTest()  {
        testRunnerService.run();
        return localizedIOService.getMessage("Shell.info.finished");
    }

}
