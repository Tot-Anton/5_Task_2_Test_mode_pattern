package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGeneration.Registration.getRegisteredUser;
import static ru.netology.DataGeneration.Registration.getUser;
import static ru.netology.DataGeneration.getRandomLogin;
import static ru.netology.DataGeneration.getRandomPassword;

public class DataEntryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    String errorMessage = "Ошибка! Неверно указан логин или пароль";


    @Test
        //Вход в систему если зарегистрированный активный пользователь
    void LoginToTheSystemIfTheRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("h2").shouldHave(exactText(" Личный кабинет"), Duration.ofSeconds(5));
    }

    @Test
        //вход незарегистрированным пользователем
    void loginAsAnUnregisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $("[data-test-id=error-notification] .notification__content").shouldHave(exactText(errorMessage));
    }

    @Test
        //Вход при неправильном пароле
    void loginWithAnIncorrectPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $("[data-test-id=error-notification] .notification__content").shouldHave(exactText(errorMessage));
    }

    @Test
    //неверный логин при входе
    void invalidLoginWhenLoggingIn() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $("[data-test-id=error-notification] .notification__content").shouldHave(exactText(errorMessage));
    }

    @Test
    //пытаемся войти при заблокированном пользователе
    void tryingToLogInWithBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(visible);
        $("[data-test-id=error-notification] .notification__content").shouldHave(exactText("Ошибка! Пользователь заблокирован"));
    }



}
