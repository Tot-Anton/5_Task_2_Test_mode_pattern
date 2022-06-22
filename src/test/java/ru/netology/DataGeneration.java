package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.util.Locale;

import static io.restassured.RestAssured.given;

// спецификация нужна для того, чтобы переиспользовать настройки в разных запросах
public class DataGeneration {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public DataGeneration() {
    }

    @BeforeAll
    private static void sendRequest(InputFields user) {
        // сам запрос
        given() //дано
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new InputFields(user.getLogin(), user.getPassword(), user.getStatus())) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users")
                .then() // "тогда ожидаем"
                .statusCode(200);
    }

    private static Faker faker = new Faker(new Locale("en"));

    public static String getRandomLogin() {
        return faker.name().firstName();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static InputFields getUser(String status) {
            return new InputFields(getRandomLogin(), getRandomPassword(), status);
        }

        public static InputFields getRegisteredUser(String status) {
            InputFields registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }


    }


}
