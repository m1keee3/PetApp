package ru.skirda.Presentation.Console.AdminAbilities;

import ru.skirda.Application.Abstractions.Repositories.IUserRepository;
import ru.skirda.Application.Exceptions.LoginExceptions.UserAlreadyExistException;
import ru.skirda.Presentation.Console.IScenario;

import java.util.Scanner;

/**
 * Сценарий для добавления нового пользователя.
 */
public class AddUserScenario implements IScenario {

    private final IUserRepository userRepository;

    /**
     * Конструктор сценария
     *
     * @param userRepository Репозиторий пользователей.
     */
    public AddUserScenario(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getName() {
        return "Add user";
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter user id: ");
            long id = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Enter user password: ");
            long password = scanner.nextLong();
            scanner.nextLine();

            userRepository.insertUser(id, password);
            System.out.println("User added successfully");

        } catch (UserAlreadyExistException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.print("Ok (press Enter to continue...");
            scanner.nextLine();
            clearConsole();
        }
    }

    private void clearConsole() {
        for (int i = 0; i < 50; i++)
            System.out.println();
    }
}