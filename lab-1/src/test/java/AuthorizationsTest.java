import org.junit.Test;
import ru.skirda.Application.Application.Users.CurrentUserManager;
import ru.skirda.Application.Application.Users.UserService;
import ru.skirda.Application.Exceptions.LoginExceptions.NotFoundException;
import ru.skirda.Application.Exceptions.LoginExceptions.UserAlreadyExistException;
import ru.skirda.Application.Exceptions.LoginExceptions.WrongAdminNameException;
import ru.skirda.Application.Exceptions.LoginExceptions.WrongPasswordException;
import ru.skirda.Infrastructure.Repository.UserRepositoryInMemory;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;


public class AuthorizationsTest {
    private final UserRepositoryInMemory userRepo = new UserRepositoryInMemory();
    private final CurrentUserManager currentUser = new CurrentUserManager();
    private final UserService userService = new UserService(userRepo, currentUser);

    @BeforeEach
    void clearRepo() throws UserAlreadyExistException {
        userRepo.clear();
    }

    @Test
    public void insertUser_WithUniqueId_ShouldNotThrow() {
        assertDoesNotThrow(() -> userRepo.insertUser(1L, 1234L));
    }

    @Test
    public void insertUser_WithDuplicateId_ShouldThrowUserAlreadyExist() {
        assertDoesNotThrow(() -> userRepo.insertUser(1L, 1234L));

        assertThrows(UserAlreadyExistException.class,
                () -> userRepo.insertUser(1L, 5678L));
    }

    @Test
    public void loginUser_WithValidCredentials_ShouldNotThrow() throws UserAlreadyExistException {
        userRepo.insertUser(1L, 1234L);
        assertDoesNotThrow(() -> userService.login(1L, 1234L));
    }

    @Test
    public void loginUser_WithInvalidId_ShouldThrowUserNotFound() {
        assertThrows(NotFoundException.class,
                () -> userService.login(999L, 1234L));
    }

    @Test
    public void loginUser_WithWrongPassword_ShouldThrowWrongPassword() throws UserAlreadyExistException {
        userRepo.insertUser(1L, 1234L);
        assertThrows(WrongPasswordException.class,
                () -> userService.login(1L, 0000L));
    }

    @Test
    public void loginAdmin_WithValidCredentials_ShouldNotThrow() {
        assertDoesNotThrow(() ->
                userService.loginAdmin("admin", "123123"));
    }

    @Test
    public void loginAdmin_WithInvalidName_ShouldThrowAdminNotFound() {
        assertThrows(WrongAdminNameException.class,
                () -> userService.loginAdmin("wrongadmin", "123123"));
    }

    @Test
    public void loginAdmin_WithWrongPassword_ShouldThrowWrongPassword() {
        assertThrows(WrongPasswordException.class,
                () -> userService.loginAdmin("admin", "wrongpass"));
    }
}