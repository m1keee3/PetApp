package ru.skirda.Presentation.Console.Quit;

import ru.skirda.Presentation.Console.IScenario;
import ru.skirda.Presentation.Console.IScenarioProvider;

import java.util.Optional;

/**
 * Провайдер сценария завершения работы приложения.
 */
public class QuitScenarioProvider implements IScenarioProvider {

    /**
     * Всегда предоставляет сценарий завершения работы.
     *
     * @return Optional содержащий сценарий QuitScenario
     */
    @Override
    public Optional<IScenario> tryGetScenario() {
        return Optional.of(new QuitScenario());
    }
}