import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skirda.Application.Application.Users.CurrentUserManager;
import ru.skirda.Application.Application.Users.UserService;
import ru.skirda.Application.Exceptions.LoginExceptions.NotFoundException;
import ru.skirda.Application.Exceptions.LoginExceptions.UserAlreadyExistException;
import ru.skirda.Application.Exceptions.OperationExceptions.InsufficientFundsException;
import ru.skirda.Application.Exceptions.OperationExceptions.InvalidAmountException;
import ru.skirda.Infrastructure.Repository.UserRepositoryInMemory;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class OperationsTest {
    private UserRepositoryInMemory userRepo;

    @BeforeEach
    void setup() throws UserAlreadyExistException, InvalidAmountException, NotFoundException {
        userRepo = new UserRepositoryInMemory();
        CurrentUserManager currentUser = new CurrentUserManager();
        UserService userService = new UserService(userRepo, currentUser);

        userRepo.insertUser(1L, 1234L);
        userRepo.addMoney(1L, BigDecimal.valueOf(1000));
    }

    @Test
    void addNegativeAmount_ShouldThrowInvalidAmount() {
        assertThrows(InvalidAmountException.class,
                () -> userRepo.addMoney(1L, BigDecimal.valueOf(-100)));
    }

    @Test
    void removeMoreThanBalance_ShouldThrowInsufficientFunds() {
        assertThrows(InsufficientFundsException.class,
                () -> userRepo.removeMoney(1L, BigDecimal.valueOf(1500)));
    }

    @Test
    void removeFromNonExistingUser_ShouldThrowUserNotFound() {
        assertThrows(NotFoundException.class,
                () -> userRepo.removeMoney(999L, BigDecimal.valueOf(100)));
    }

    @Test
    void validOperations_ShouldNotThrow() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> userRepo.addMoney(1L, BigDecimal.valueOf(500))),

                () -> assertDoesNotThrow(
                        () -> userRepo.removeMoney(1L, BigDecimal.valueOf(300)))
        );
    }

    @Test
    void complexOperationFlow() {
        // Начальный баланс: 1000
        assertDoesNotThrow(() -> {
            userRepo.removeMoney(1L, BigDecimal.valueOf(500)); // 500 осталось
            userRepo.addMoney(1L, BigDecimal.valueOf(200));    // 700
            userRepo.removeMoney(1L, BigDecimal.valueOf(700)); // 0
        });

        // Попытка снять еще 100
        assertThrows(InsufficientFundsException.class,
                () -> userRepo.removeMoney(1L, BigDecimal.valueOf(100))
        );
    }
}