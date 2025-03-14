package ru.skirda.Presentation.Console.User;

import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Models.Users.UserRole;
import ru.skirda.Presentation.Console.IScenario;
import ru.skirda.Presentation.Console.IScenarioProvider;

import java.util.Optional;

/**
 * Провайдер сценария отображения баланса
 */
public class ShowBalanceScenarioProvider implements IScenarioProvider {

    private final ICurrentUserService currentUserService;

    /**
     * Конструктор провайдера
     *
     * @param currentUserService Сервис для работы с текущим пользователем
     */
    public ShowBalanceScenarioProvider(ICurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @Override
    public Optional<IScenario> tryGetScenario() {
        return currentUserService.getUser()
                .filter(user -> user.getRole() == UserRole.USER)
                .map(user -> new ShowBalanceScenario(currentUserService));
    }
}