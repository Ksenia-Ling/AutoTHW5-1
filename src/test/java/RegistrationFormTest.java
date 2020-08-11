import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class RegistrationFormTest {

    @Test
    void shouldRegisterActiveUser() {
        UserInfo user = DataGenerator.getUser("active");

        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$(".button").click();
        $$(".heading").find(exactText("Личный кабинет")).shouldBe(exist);
    }

    @Test
    void shouldNotRegisterBlockedUser() {
        UserInfo user = DataGenerator.getUser("blocked");

        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$(".button").click();
        $(withText("Пользователь заблокирован")).shouldBe(exist);
    }

    @Test
    void shouldNotRegisterInvalidLogin() {
        UserInfo user = DataGenerator.getUser("active");

        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(DataGenerator.getLogin("en"));
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$(".button").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(exist);
    }

    @Test
    void shouldNotRegisterInvalidPassword() {
        UserInfo user = DataGenerator.getUser("active");

        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(DataGenerator.getPassword("en"));
        form.$(".button").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(exist);
    }
}
