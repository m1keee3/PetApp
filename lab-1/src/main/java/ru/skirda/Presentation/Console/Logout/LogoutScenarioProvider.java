package ru.skirda.Presentation.Console.Logout;

import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Contracts.Users.IUserService;
import ru.skirda.Presentation.Console.IScenario;
import ru.skirda.Presentation.Console.IScenarioProvider;

import java.util.Optional;

/**
 * Провайдер сценария выхода из системы.
 */
public class LogoutScenarioProvider implements IScenarioProvider {

    private final IUserService userService;
    private final ICurrentUserService currentUserService;

    public LogoutScenarioProvider(
            IUserService userService,
            ICurrentUserService currentUserService
    ) {
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @Override
    public Optional<IScenario> tryGetScenario() {
        if (currentUserService.getUser().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new LogoutScenario(userService));
    }
}