import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static String login(String Locale) {
        Faker faker = new Faker(new Locale(Locale));
        return faker.name().firstName();
    }

    public static String password(String Locale) {
        Faker faker = new Faker(new Locale(Locale));
        return faker.internet().password();
    }

    public static UserInfo getUser(String status) {

        UserInfo user = new UserInfo(login("en"),
                password("en"), status);
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
        return user;
    }

    public static UserInfo getInvalidLoginUser(String status) {
        String password = password("en");
        getUser(status);

        return new UserInfo("Table", password, status);
    }

    public static UserInfo getInvalidPasswordUser(String status) {
        String login = login("en");
        getUser(status);

        return new UserInfo(login, "theSimplestPassword", status);
    }
}
