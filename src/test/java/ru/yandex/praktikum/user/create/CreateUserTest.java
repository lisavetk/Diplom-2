package ru.yandex.praktikum.user.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import ru.yandex.praktikum.user.GeneratorUser;
import ru.yandex.praktikum.user.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static ru.yandex.praktikum.TestsConstants.CREATE_USER_FIELD_MESSAGE_EXISTS_DATA;
import static ru.yandex.praktikum.TestsMessage.*;

@DisplayName("Create user")
public class CreateUserTest {
    UserSteps userSteps = new UserSteps();
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @Test
    @DisplayName("Create user with valid data")
    @Description("Return 200 OK and valid response")
    public void createUserReturn200DataIsValid() {
        CreateUserRequest request = GeneratorUser.getRandomUser();
        Response response = userSteps.createUser(request);
        response.then().statusCode(SC_OK);

        CreateUserSuccessResponse createUserSuccessResponse = response.as(CreateUserSuccessResponse.class);

        assertTrue(MESSAGE_SUCCESS_IS_NOT_TRUE, createUserSuccessResponse.isSuccess());
        assertEquals(MESSAGE_EMAIL_NOT_MATCH, request.getEmail(), createUserSuccessResponse.getUser().getEmail());
        assertEquals(MESSAGE_USER_NAME_NOT_MATCH, request.getName(), createUserSuccessResponse.getUser().getName());
        assertNotNull(MESSAGE_ACCESS_TOKEN_IS_NULL, createUserSuccessResponse.getAccessToken());
        assertNotNull(MESSAGE_REFRESH_TOKEN_IS_NULL, createUserSuccessResponse.getRefreshToken());

        accessToken = createUserSuccessResponse.getAccessToken();
    }

    @Test
    @DisplayName("Create user with exists data")
    @Description("Return 403 Forbidden and valid response")
    public void createUserReturn403DataIsExists() {
        CreateUserRequest request = GeneratorUser.getRandomUser();
        userSteps.createUser(request);
        Response response = userSteps.createUser(request);
        response.then().statusCode(SC_FORBIDDEN);

        CreateUserForbiddenResponse userForbiddenResponse = response.as(CreateUserForbiddenResponse.class);

        assertFalse(MESSAGE_SUCCESS_IS_NOT_FALSE, userForbiddenResponse.isSuccess());
        assertEquals(MESSAGE_FIELD_MESSAGE_NOT_MATCH, CREATE_USER_FIELD_MESSAGE_EXISTS_DATA, userForbiddenResponse.getMessage());
    }


    @After
    @DisplayName("Delete created user")
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }

}
