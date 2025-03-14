package ru.skirda.Presentation.Console.Login;

import ru.skirda.Application.Exceptions.LoginExceptions.NotFoundException;
import ru.skirda.Application.Exceptions.LoginExceptions.WrongPasswordException;
import ru.skirda.Application.Contracts.Users.IUserService;
import ru.skirda.Presentation.Console.IScenario;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Сценарий входа пользователя в систему.
 */
public class UserLoginScenario implements IScenario {

    private final IUserService userService;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор сценария
     *
     * @param userService Сервис для работы с пользователями
     */
    public UserLoginScenario(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return "User login";
    }

    @Override
    public void run() {
        try {
            System.out.print("Enter your user id: ");
            long userId = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Enter your user password: ");
            long password = scanner.nextLong();
            scanner.nextLine();

            userService.login(userId, password);
            System.out.println("Successful login");

        } catch (NotFoundException | WrongPasswordException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format");
        } finally {
            System.out.print("Ok (press Enter to continue)...");
            scanner.nextLine();
            clearConsole();
        }
    }

    private void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}