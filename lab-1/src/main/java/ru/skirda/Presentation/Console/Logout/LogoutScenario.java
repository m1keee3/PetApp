package ru.skirda.Presentation.Console.Logout;

import ru.skirda.Application.Contracts.Users.IUserService;
import ru.skirda.Presentation.Console.IScenario;

import java.util.Optional;
import java.util.Scanner;

/**
 * Сценарий выхода из системы.
 */
public class LogoutScenario implements IScenario {

    private final IUserService userService;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор сценария
     *
     * @param userService Сервис для работы с пользователями
     */
    public LogoutScenario(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return "Logout";
    }

    @Override
    public void run() {
        userService.logout();
        System.out.print("Ok (press Enter to continue)...");
        scanner.nextLine();
        clearConsole();
    }

    private void clearConsole() {
        for (int i = 0; i < 50; i++)
            System.out.println();
    }
}