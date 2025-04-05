package ru.skirda;

import ru.skirda.Presentation.Console.ScenarioRunner;
import ru.skirda.config.DependencyConfig;

/**
 * Main class
 */
public class Main {
    /**
     * Устанавливает зависимости используя DependencyConfig
     * Запускает цикл операций
     * @param args
     */
    public static void main(String[] args) {
        ScenarioRunner runner = DependencyConfig.configure();

        while (true) {
            runner.run();
        }

    }
}