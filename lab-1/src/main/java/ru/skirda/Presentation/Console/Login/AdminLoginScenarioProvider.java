package ru.skirda.Presentation.Console.Login;

import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Contracts.Users.IUserService;
import ru.skirda.Presentation.Console.IScenario;
import ru.skirda.Presentation.Console.IScenarioProvider;

import java.util.Optional;

/**
 * Провайдер сценария входа администратора в систему.
 */
public class AdminLoginScenarioProvider implements IScenarioProvider {

    private final IUserService userService;
    private final ICurrentUserService currentUserService;

    /**
     * Конструктор провайдера сценария.
     *
     * @param userService        сервис работы с пользователями
     * @param currentUserService сервис текущего пользователя
     */
    public AdminLoginScenarioProvider(
            IUserService userService,
            ICurrentUserService currentUserService
    ) {
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @Override
    public Optional<IScenario> tryGetScenario() {
        if (currentUserService.getUser().isPresent()) {
            return Optional.empty();
        }

        return Optional.of(new AdminLoginScenario(userService));
    }
}