package ru.skirda.Presentation.Console.User;

import ru.skirda.Application.Abstractions.Repositories.IOperationRepository;
import ru.skirda.Application.Contracts.Operations.IOperationService;
import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Models.Operations.Operation;
import ru.skirda.Application.Models.Users.User;
import ru.skirda.Presentation.Console.IScenario;
import java.util.List;
import java.util.Scanner;

/**
 * Сценарий отображения истории операций
 */
public class ShowHistoryScenario implements IScenario {

    private final IOperationService operationRepository;
    private final ICurrentUserService currentUserService;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор сценария
     *
     * @param operationRepository Репозиторий операций
     * @param currentUserService Сервис для работы с текущим пользователем
     */
    public ShowHistoryScenario(
            IOperationService operationRepository,
            ICurrentUserService currentUserService
    ) {
        this.operationRepository = operationRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public String getName() {
        return "Show history";
    }

    @Override
    public void run() {
        User user = currentUserService.getUser()
                .orElseThrow(() -> new IllegalStateException("User not authenticated"));

        List<Operation> operations = operationRepository.getOperationsByUserId(user.getId());

        System.out.println("\nOperation history:");
        operations.forEach(op -> System.out.println(op.toString()));

        System.out.print("\nOk (press Enter to continue)...");
        scanner.nextLine();
        clearConsole();
    }

    private void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}

