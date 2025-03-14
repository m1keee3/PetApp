package ru.skirda.Presentation.Console.Quit;

import ru.skirda.Presentation.Console.IScenario;

/**
 * Сценарий завершения работы приложения.
 */
public class QuitScenario implements IScenario {

    /**
     * Возвращает название сценария.
     *
     * @return Название сценария "Quit"
     */
    @Override
    public String getName() {
        return "Quit";
    }

    /**
     * Выполняет завершение работы приложения.
     */
    @Override
    public void run() {
        System.exit(0);
    }
}