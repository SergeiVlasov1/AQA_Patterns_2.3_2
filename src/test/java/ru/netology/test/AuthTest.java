package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.Auth;
import ru.netology.data.DataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    public void openPage() {
        open("http://localhost:9999");
    }

    DataGenerator.UserInfo active = DataGenerator.getUserInfo("active");
    DataGenerator.UserInfo blocked = DataGenerator.getUserInfo("blocked");

    public void registration(String login, String password) {
        $("[name=login]").setValue(login);
        $("[name=password]").setValue(password);
        $(".button").click();
    }

    @Test
    @DisplayName("Should successfully login if active user exist")
    public void shouldSuccessfulLoginIfUserExist() {

        Auth.createUser(active);
        registration(active.getLogin(), active.getPassword());
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should show error message if user trying to sing in with wrong login")
    public void shouldNotSignForInvalidLogin() {

        Auth.createUser(active);
        registration(DataGenerator.getInvalidLogin(), active.getPassword());
        $(".notification__title").shouldHave(text("Ошибка")).shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should show error message if user trying to sing in with wrong password")
    public void shouldNotSignForInvalidPassword() {

        Auth.createUser(active);
        registration(active.getLogin(), DataGenerator.getInvalidPassword());
        $(".notification__title").shouldHave(text("Ошибка")).shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    public void shouldGetErrorIfBlockedUser() {

        Auth.createUser(blocked);
        registration(blocked.getLogin(), blocked.getPassword());
        $(".notification__title").shouldHave(text("Ошибка")).shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! Пользователь заблокирован")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should show error if user doesn't exist")
    public void shouldNotSignInIfDoesNotUserExist() {

        registration(blocked.getLogin(), blocked.getPassword());
        $(".notification__title").shouldHave(text("Ошибка")).shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }
}
