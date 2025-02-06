package ru.netology.deliveri.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static ru.netology.deliveri.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.deliveri.data.DataGenerator.Registration.getUser;
import static ru.netology.deliveri.data.DataGenerator.getRandomLogin;
import static ru.netology.deliveri.data.DataGenerator.getRandomPassword;

public class AppIbankTest {

    @BeforeEach
    void setUp() {
        Selenide.open("http://localhost:9999");
    }
    @Test
    void shouldSuccessfulLoginIfRegisteredUser() {
        var regUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(regUser.getLogin());
        $("[data-test-id=password] input").setValue(regUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("h2").should(Condition.exactText("Личный кабинет"), Condition.visible);
    }

    @Test
    void shouldNoSuccessfulLoginIfNoValidLogin() {
        var regUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(regUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .should(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Condition.visible);
    }

    @Test
    void shouldNoSuccessfulLoginIfNoValidPassword() {
        var regUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").setValue(regUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .should(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Condition.visible);
    }

    @Test
    void shouldNoSuccessfulLoginIfNoRegisteredUser() {
        var noRegUser = getUser("active");
        $("[data-test-id=login] input").setValue(noRegUser.getLogin());
        $("[data-test-id=password] input").setValue(noRegUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .should(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Condition.visible);
    }

    @Test
    void shouldNoSuccessfulLoginIfBlockedUser() {
        var noRegUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(noRegUser.getLogin());
        $("[data-test-id=password] input").setValue(noRegUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .should(Condition.exactText("Ошибка! Пользователь заблокирован"), Condition.visible);
    }
}
