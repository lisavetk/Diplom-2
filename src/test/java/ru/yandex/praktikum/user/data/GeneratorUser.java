package ru.yandex.praktikum.user.data;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.praktikum.user.create.request.CreateUserRequest;

public class GeneratorUser {

    @Step("Create user's ransom data: email, name, password")
    public static CreateUserRequest getRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@test.com";
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new CreateUserRequest(email, password, name);
    }

    @Step("Create user's ransom data: name, password; email is null")
    public static CreateUserRequest getUserWithoutEmail() {
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new CreateUserRequest(null, password, name);
    }

    @Step("Create user's ransom data: email, name; password is null")
    public static CreateUserRequest getUserWithoutPassword() {
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@test.com";
        String name = RandomStringUtils.randomAlphabetic(10);
        return new CreateUserRequest(email, null, name);
    }

    @Step("Create user's ransom data: email, password; name is null")
    public static CreateUserRequest getUserWithoutName() {
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@test.com";
        String password = RandomStringUtils.randomAlphabetic(10);
        return new CreateUserRequest(email, password, null);
    }

}
