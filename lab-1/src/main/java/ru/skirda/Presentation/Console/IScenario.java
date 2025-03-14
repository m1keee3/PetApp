package ru.skirda.Presentation.Console;

/**
 * Интерфейс для сценария
 */
public interface IScenario {
    /**
     * Возвращает имя сценария
     *
     * @return Имя сценария
     */
    String getName();

    /**
     * Запускает выполнение сценария
     */
    void run();
}
