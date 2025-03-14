package ru.skirda.Application.Contracts.Users;

import ru.skirda.Application.Exceptions.LoginExceptions.NotFoundException;
import ru.skirda.Application.Exceptions.LoginExceptions.WrongAdminNameException;
import ru.skirda.Application.Exceptions.LoginExceptions.WrongPasswordException;

/**
 * Интерфейс сервиса для работы с авторизацией пользователей.
 */
public interface IUserService {


    /**
     * Выполняет вход пользователя в систему.
     *
     * @param id идентификатор пользователя
     * @param password пароль пользователя
     * @throws NotFoundException если пользователь с таким идентификатором не найден
     * @throws WrongPasswordException если введен неверный пароль
     */
    void login(long id, long password) throws NotFoundException, WrongPasswordException;


    /**
     * Выполняет вход администратора в систему.
     *
     * @param name имя администратора
     * @param password пароль администратора
     * @throws WrongAdminNameException если введено неправильное имя администратора
     * @throws WrongPasswordException если введен неправильный пароль для администратора
     */
    void loginAdmin(String name, String password) throws WrongAdminNameException, WrongPasswordException;

    /**
     * Выполняет выход текущего пользователя из системы.
     */
    void logout();
}