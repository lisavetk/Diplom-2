package ru.yandex.praktikum.user.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.user.GeneratorUser;
import ru.yandex.praktikum.user.UserSteps;
import ru.yandex.praktikum.user.create.CreateUserRequest;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;
import static ru.yandex.praktikum.TestsConstants.LOGIN_USER_FIELD_MESSAGE_INVALID_DATA;
import static ru.yandex.praktikum.TestsMessage.*;

@DisplayName("Login user with invalid data")
@RunWith(Parameterized.class)
public class LoginUserNegativeTest {
    UserSteps userSteps = new UserSteps();
    String accessToken;
    CreateUserRequest createUserRequest;
    String email;
    String password;
    String description;

    public LoginUserNegativeTest(CreateUserRequest createUserRequest, String email, String password, String description) {
        this.createUserRequest = createUserRequest;
        this.email = email;
        this.password = password;
        this.description = description;
    }

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        accessToken = userSteps.createUser(createUserRequest).path("accessToken");
    }

    @Parameterized.Parameters(name = "{3}")
    public static Object[][] testData() {
        CreateUserRequest user = GeneratorUser.getRandomUser();

        return new Object[][] {
                {user, null, user.getPassword(), "null email"},
                {user, user.getEmail(), null, "null password"},
                {user, "invalid@example.com", user.getPassword(), "invalid email"},
                {user, user.getEmail(), "wrongPassword", "invalid password"}
        };
    }


    @Test
    @DisplayName("Login user with invalid data")
    @Description("Return 401 Unauthorized and valid response")
    public void loginUserReturn401WithInvalidData() {
        LoginUserRequest request = new LoginUserRequest(email, password);
        Response response = userSteps.loginUser(request);
        response.then().statusCode(SC_UNAUTHORIZED);

        LoginUserUnauthorizedResponse loginUserUnauthorizedResponse = response.as(LoginUserUnauthorizedResponse.class);
        assertFalse(MESSAGE_SUCCESS_IS_NOT_FALSE, loginUserUnauthorizedResponse.isSuccess());
        assertEquals(MESSAGE_FIELD_MESSAGE_NOT_MATCH, LOGIN_USER_FIELD_MESSAGE_INVALID_DATA, loginUserUnauthorizedResponse.getMessage());
    }


    @After
    @DisplayName("Delete created user")
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }

}
