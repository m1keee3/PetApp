package ru.skirda.config;

import ru.skirda.Application.Application.Operations.OperationService;
import ru.skirda.Application.Application.Users.CurrentUserManager;
import ru.skirda.Application.Application.Users.UserService;
import ru.skirda.Infrastructure.Repository.OperationRepositoryInMemory;
import ru.skirda.Infrastructure.Repository.UserRepositoryInMemory;
import ru.skirda.Presentation.Console.AdminAbilities.AddUserScenarioProvider;
import ru.skirda.Presentation.Console.IScenarioProvider;
import ru.skirda.Presentation.Console.Login.AdminLoginScenarioProvider;
import ru.skirda.Presentation.Console.Login.UserLoginScenarioProvider;
import ru.skirda.Presentation.Console.Logout.LogoutScenarioProvider;
import ru.skirda.Presentation.Console.Quit.QuitScenarioProvider;
import ru.skirda.Presentation.Console.ScenarioRunner;
import ru.skirda.Presentation.Console.User.PutMoneyScenarioProvider;
import ru.skirda.Presentation.Console.User.RemoveMoneyScenarioProvider;
import ru.skirda.Presentation.Console.User.ShowBalanceScenarioProvider;
import ru.skirda.Presentation.Console.User.ShowHistoryScenarioProvider;

import java.util.List;

/**
 * Класс определяющий зависимости между слоями Application, Infrastructure, Presentation
 */
public class DependencyConfig {
    public static ScenarioRunner configure() {
        UserRepositoryInMemory userRepo = new UserRepositoryInMemory();
        OperationRepositoryInMemory operationRepo = new OperationRepositoryInMemory();

        CurrentUserManager currentUserManager = new CurrentUserManager();
        UserService userService = new UserService(userRepo, currentUserManager);
        OperationService operationService = new OperationService(operationRepo);

        List<IScenarioProvider> providers = List.of(
                new UserLoginScenarioProvider(userService, currentUserManager),
                new AdminLoginScenarioProvider(userService, currentUserManager),
                new LogoutScenarioProvider(userService, currentUserManager),
                new AddUserScenarioProvider(userRepo, currentUserManager),
                new PutMoneyScenarioProvider(userRepo, currentUserManager, operationService),
                new RemoveMoneyScenarioProvider(userRepo, currentUserManager, operationService),
                new ShowBalanceScenarioProvider(currentUserManager),
                new ShowHistoryScenarioProvider(operationService, currentUserManager),
                new QuitScenarioProvider()
        );

        return new ScenarioRunner(providers);
    }
}