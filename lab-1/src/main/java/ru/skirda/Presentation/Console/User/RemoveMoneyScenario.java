package ru.skirda.Presentation.Console.User;

import ru.skirda.Application.Abstractions.Repositories.IUserRepository;
import ru.skirda.Application.Contracts.Operations.IOperationService;
import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Exceptions.LoginExceptions.NotFoundException;
import ru.skirda.Application.Exceptions.OperationExceptions.InsufficientFundsException;
import ru.skirda.Application.Exceptions.OperationExceptions.InvalidAmountException;
import ru.skirda.Application.Models.Operations.OperationType;
import ru.skirda.Application.Models.Users.User;
import ru.skirda.Presentation.Console.IScenario;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Сценарий для снимания денег со счета
 */
public class RemoveMoneyScenario implements IScenario {

    private final ICurrentUserService currentUserService;
    private final IUserRepository userRepository;
    private final IOperationService operationService;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор сценария
     *
     * @param currentUserService Сервис для работы с текущим пользователем
     * @param userRepository Репозиторий пользователей
     * @param operationService Сервис для работы с операциями
     */
    public RemoveMoneyScenario(
            ICurrentUserService currentUserService,
            IUserRepository userRepository,
            IOperationService operationService
    ) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.operationService = operationService;
    }

    @Override
    public String getName() {
        return "Remove money";
    }

    @Override
    public void run() {
        try {
            System.out.print("Enter amount: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine());

            User user = currentUserService.getUser()
                    .orElseThrow(() -> new IllegalStateException("User not authenticated"));

            userRepository.removeMoney(user.getId(), amount);
            System.out.println("Money removed successfully");
            operationService.addOperation(user.getId(), OperationType.Remove, amount);

        } catch (InsufficientFundsException e) {
            System.out.printf("Error: " + e.getMessage());
        } catch (InvalidAmountException | NotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            System.out.print("Ok (press Enter to continue)...");
            scanner.nextLine();
            clearConsole();
        }
    }

    private void clearConsole() {
        for (int i = 0; i < 50; i++)
            System.out.println();
    }
}