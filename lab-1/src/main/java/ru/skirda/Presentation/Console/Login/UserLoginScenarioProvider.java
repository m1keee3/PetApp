package ru.skirda.Presentation.Console.Login;

import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Contracts.Users.IUserService;
import ru.skirda.Presentation.Console.IScenario;
import ru.skirda.Presentation.Console.IScenarioProvider;

import java.util.Optional;

/**
 * Провайдер сценария входа пользователя в систему.
 */
public class UserLoginScenarioProvider implements IScenarioProvider {

    private final IUserService userService;
    private final ICurrentUserService currentUserService;

    /**
     * Конструктор провайдера сценария.
     *
     * @param userService        Сервис для работы с пользователями
     * @param currentUserService Сервис текущего пользователя
     */
    public UserLoginScenarioProvider(
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

        return Optional.of(new UserLoginScenario(userService));
    }
}