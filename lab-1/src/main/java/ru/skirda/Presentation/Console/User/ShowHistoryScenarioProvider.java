package ru.skirda.Presentation.Console.User;

import ru.skirda.Application.Contracts.Operations.IOperationService;
import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Models.Users.UserRole;
import ru.skirda.Presentation.Console.IScenario;
import ru.skirda.Presentation.Console.IScenarioProvider;

import java.util.Optional;

/**
 * Провайдер сценария отображения истории операций
 */
public class ShowHistoryScenarioProvider implements IScenarioProvider {

    private final ICurrentUserService currentUserService;
    private final IOperationService operationService;

    /**
     * Конструктор провайдера
     *
     * @param operationRepository Репозиторий операций
     * @param currentUserService Сервис для работы с текущим пользователем
     */
    public ShowHistoryScenarioProvider(
            IOperationService operationRepository,
            ICurrentUserService currentUserService
    ) {
        this.operationService = operationRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public Optional<IScenario> tryGetScenario() {
        return currentUserService.getUser()
                .filter(user -> user.getRole() == UserRole.USER)
                .map(user -> new ShowHistoryScenario(operationService, currentUserService));
    }
}
