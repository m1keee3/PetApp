package ru.skirda.Presentation.Console.AdminAbilities;

import ru.skirda.Application.Abstractions.Repositories.IUserRepository;
import ru.skirda.Application.Contracts.Users.ICurrentUserService;
import ru.skirda.Application.Models.Users.UserRole;
import ru.skirda.Presentation.Console.IScenario;
import ru.skirda.Presentation.Console.IScenarioProvider;
import java.util.Optional;

/**
 * Провайдер сценария для добавления нового пользователя.
 * Проверяет, имеет ли текущий пользователь права администратора.
 */
public class AddUserScenarioProvider implements IScenarioProvider {

    private final IUserRepository userRepository;
    private final ICurrentUserService currentUser;

    /**
     * Конструктор провайдера сценария.
     *
     * @param userRepository Репозиторий пользователей.
     * @param currentUser    Сервис текущего пользователя.
     */
    public AddUserScenarioProvider(IUserRepository userRepository, ICurrentUserService currentUser) {
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @Override
    public Optional<IScenario> tryGetScenario() {
        if (currentUser.getUser().isEmpty() || currentUser.getUser().get().getRole() != UserRole.ADMIN) {
            return Optional.empty();
        }

        return Optional.of(new AddUserScenario(userRepository));
    }
}