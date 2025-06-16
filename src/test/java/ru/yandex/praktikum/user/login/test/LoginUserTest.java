package ru.yandex.praktikum.user.login.test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.user.data.GeneratorUser;
import ru.yandex.praktikum.user.UserSteps;
import ru.yandex.praktikum.user.create.request.CreateUserRequest;
import ru.yandex.praktikum.user.login.request.LoginUserRequest;
import ru.yandex.praktikum.user.login.response.LoginUserSuccessResponse;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;
import static ru.yandex.praktikum.config.TestsMessage.*;

@DisplayName("Login user")
public class LoginUserTest {
    UserSteps userSteps = new UserSteps();
    String accessToken;
    CreateUserRequest createUserRequest;

    @Before
    @DisplayName("Create user for tests")
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        createUserRequest = GeneratorUser.getRandomUser();
        accessToken = userSteps.createUser(createUserRequest).path("accessToken");
    }

    @Test
    @DisplayName("Login user with valid data")
    @Description("Return 200 OK and valid response")
    public void loginUserReturn200WithValidData() {
        LoginUserRequest request = new LoginUserRequest(createUserRequest.getEmail(), createUserRequest.getPassword());
        Response response = userSteps.loginUser(request);
        response.then().statusCode(SC_OK);

        LoginUserSuccessResponse loginUserSuccessResponse = response.as(LoginUserSuccessResponse.class);
        assertTrue(MESSAGE_SUCCESS_IS_NOT_TRUE, loginUserSuccessResponse.isSuccess());
        assertNotNull(MESSAGE_ACCESS_TOKEN_IS_NULL, loginUserSuccessResponse.getAccessToken());
        assertNotNull(MESSAGE_REFRESH_TOKEN_IS_NULL, loginUserSuccessResponse.getRefreshToken());
        assertEquals(MESSAGE_EMAIL_NOT_MATCH, createUserRequest.getEmail(), loginUserSuccessResponse.getUser().getEmail());
        assertEquals(MESSAGE_USER_NAME_NOT_MATCH, createUserRequest.getName(), loginUserSuccessResponse.getUser().getName());

    }

    @After
    @DisplayName("Delete created user")
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
