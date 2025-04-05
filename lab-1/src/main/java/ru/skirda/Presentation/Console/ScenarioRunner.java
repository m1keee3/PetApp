package ru.skirda.Presentation.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Класс ScenarioRunner предназначен для запуска сценариев, предоставленных провайдерами сценариев
 */
public class ScenarioRunner {

    private final List<IScenarioProvider> providers;

    /**
     * Конструктор класса ScenarioRunner
     *
     * @param providers Список провайдеров сценариев
     */
    public ScenarioRunner(List<IScenarioProvider> providers) {
        this.providers = providers;
    }

    /**
     * Запускает процесс выбора и выполнения сценария
     */
    public void run() {
        List<IScenario> scenarios = getScenarios();

        if (scenarios.isEmpty()) {
            System.out.println("No available scenarios.");
            return;
        }

        System.out.println("Select action:");
        for (int i = 0; i < scenarios.size(); i++) {
            System.out.println((i + 1) + ". " + scenarios.get(i).getName());
        }

        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.print("Enter choice: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < 1 || choice > scenarios.size());

        scenarios.get(choice - 1).run();
    }

    /**
     * Возвращает список доступных сценариев от всех провайдеров
     *
     * @return Список сценариев
     */
    private List<IScenario> getScenarios() {
        List<IScenario> scenarios = new ArrayList<>();
        for (IScenarioProvider provider : providers) {
            Optional<IScenario> scenario = provider.tryGetScenario();
            scenario.ifPresent(scenarios::add);
        }
        return scenarios;
    }
}