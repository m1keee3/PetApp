package ru.skirda.Application.Application.Users;

import ru.skirda.Application.Abstractions.Repositories.IUserRepository;
import ru.skirda.Application.Exceptions.LoginExceptions.NotFoundException;
import ru.skirda.Application.Exceptions.LoginExceptions.WrongAdminNameException;
import ru.skirda.Application.Exceptions.LoginExceptions.WrongPasswordException;
import ru.skirda.Application.Contracts.Users.IUserService;
import ru.skirda.Application.Models.Users.User;
import ru.skirda.Application.Models.Users.UserRole;

import java.math.BigDecimal;

/**
 * Реализация сервиса для работы с пользователями
 */
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final CurrentUserManager currentUserManager;

    public UserService(IUserRepository userRepository, CurrentUserManager currentUserManager) {
        this.userRepository = userRepository;
        this.currentUserManager = currentUserManager;
    }

    @Override
    public void login(long id, long password)
            throws NotFoundException, WrongPasswordException {

        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException(id));

        if (user.getPassword() != password) {
            throw new WrongPasswordException();
        }

        currentUserManager.setUser(user);
    }

    @Override
    public void loginAdmin(String name, String password)
            throws WrongAdminNameException, WrongPasswordException {

        if (!"admin".equals(name)) {
            throw new WrongAdminNameException(name);
        }

        if (!"123123".equals(password)) {
            throw new WrongPasswordException();
        }

        User admin = new User(
                UserRole.ADMIN,
                0L,
                123123L,
                BigDecimal.valueOf(1000)
        );
        currentUserManager.setUser(admin);
    }

    @Override
    public void logout() {
        currentUserManager.setUser(null);
    }
}