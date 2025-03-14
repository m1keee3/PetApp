package ru.skirda.Presentation.Console.Login;

import ru.skirda.Application.Exceptions.LoginExceptions.WrongAdminNameException;
import ru.skirda.Application.Exceptions.LoginExceptions.WrongPasswordException;
import ru.skirda.Application.Contracts.Users.IUserService;
import ru.skirda.Presentation.Console.IScenario;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Сценарий входа администратора в систему.
 */
public class AdminLoginScenario implements IScenario {

    private final IUserService userService;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор сценария
     *
     * @param userService сервис работы с пользователями
     */
    public AdminLoginScenario(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return "Admin login";
    }

    @Override
    public void run() {
        try {
            System.out.print("Enter your admin name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your admin password: ");
            String password = scanner.nextLine();

            userService.loginAdmin(name, password);
            System.out.println("Successful login");

        } catch (WrongAdminNameException | WrongPasswordException e) {
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