package ru.skirda.Presentation.Console.User;

import ru.skirda.Application.Abstractions.Repositories.IOperationRepository;
import ru.skirda.Application.Abstractions.Repositories.IUserRepository;
import ru.skirda.Application.Contracts.Operations.IOperationService;
import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Models.Users.UserRole;
import ru.skirda.Presentation.Console.IScenario;
import ru.skirda.Presentation.Console.IScenarioProvider;

import java.util.Optional;

/**
 * Провайдер сценария добавления денег на счет
 */
public class PutMoneyScenarioProvider implements IScenarioProvider {

    private final ICurrentUserService currentUser;
    private final IUserRepository userRepository;
    private final IOperationService operationService;

    /**
     * Конструктор провайдера
     *
     * @param userRepository Репозиторий пользователей
     * @param currentUser Текущий пользователь
     * @param operationService Сервис для работы с операциями
     */
    public PutMoneyScenarioProvider(
            IUserRepository userRepository,
            ICurrentUserService currentUser,
            IOperationService operationService
    ) {
        this.userRepository = userRepository;
        this.currentUser = currentUser;
        this.operationService = operationService;
    }

    @Override
    public Optional<IScenario> tryGetScenario() {
        return currentUser.getUser()
                .filter(user -> user.getRole() == UserRole.USER)
                .map(user -> new PutMoneyScenario(currentUser, userRepository, operationService));
    }
}

