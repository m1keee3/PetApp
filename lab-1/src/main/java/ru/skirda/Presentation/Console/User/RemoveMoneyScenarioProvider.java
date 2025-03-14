package ru.skirda.Presentation.Console.User;

import ru.skirda.Application.Abstractions.Repositories.IUserRepository;
import ru.skirda.Application.Contracts.Operations.IOperationService;
import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Models.Users.UserRole;
import ru.skirda.Presentation.Console.IScenario;
import ru.skirda.Presentation.Console.IScenarioProvider;

import java.util.Optional;


/**
 * Провайдер сценария снимания денег со счета
 */
public class RemoveMoneyScenarioProvider implements IScenarioProvider {

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
    public RemoveMoneyScenarioProvider(
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
                .map(user -> new RemoveMoneyScenario(currentUser, userRepository, operationService));
    }
}