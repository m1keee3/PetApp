package ru.skirda.Presentation.Console.User;

import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Models.Users.User;
import ru.skirda.Presentation.Console.IScenario;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Сценарий отображения баланса пользователя
 */
public class ShowBalanceScenario implements IScenario {

    private final ICurrentUserService currentUserService;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор сценария
     *
     * @param currentUserService Сервис для работы с текущим пользователем
     */
    public ShowBalanceScenario(ICurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @Override
    public String getName() {
        return "Show balance";
    }

    @Override
    public void run() {
        User user = currentUserService.getUser()
                .orElseThrow(() -> new IllegalStateException("User not authenticated"));

        System.out.println("Current balance: " + formatBalance(user.getBalance()));

        System.out.print("Ok (press Enter to continue)...");
        scanner.nextLine();
        clearConsole();
    }

    private String formatBalance(BigDecimal balance) {
        return String.format("%.2f", balance);
    }

    private void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}