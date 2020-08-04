import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class RegistrationFormTest {

    @Test
    void shouldRegisterActiveUser() {
        UserInfo registration = DataGenerator.getUser("active");
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(registration.getLogin());
        form.$("[data-test-id=password] input").setValue(registration.getPassword());
        form.$(".button").click();
        $$(".heading").find(exactText("Личный кабинет")).shouldBe(exist);
    }

    @Test
    void shouldNotRegisterBlockedUser() {
        UserInfo registration = DataGenerator.getUser("blocked");
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(registration.getLogin());
        form.$("[data-test-id=password] input").setValue(registration.getPassword());
        form.$(".button").click();
        $(withText("Пользователь заблокирован")).shouldBe(exist);
    }

    @Test
    void shouldNotRegisterInvalidLogin() {
        UserInfo registration = DataGenerator.getUser("active");
        Faker faker = new Faker();
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(faker.name().firstName());
        form.$("[data-test-id=password] input").setValue(registration.getPassword());
        form.$(".button").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(exist);
    }

    @Test
    void shouldNotRegisterInvalidPassword() {
        UserInfo registration = DataGenerator.getUser("active");
        Faker faker = new Faker();
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(registration.getLogin());
        form.$("[data-test-id=password] input").setValue(faker.internet().password());
        form.$(".button").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(exist);
    }
}
