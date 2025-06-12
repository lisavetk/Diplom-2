package ru.yandex.praktikum.user.create;

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

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static ru.yandex.praktikum.TestsConstants.CREATE_USER_FIELD_MESSAGE_NULL_FIELD;
import static ru.yandex.praktikum.TestsMessage.*;

@DisplayName("Create user without data")
@RunWith(Parameterized.class)
public class CreateUserNegativeTest {
    UserSteps userSteps = new UserSteps();
    String accessToken;

    String descriptions;
    CreateUserRequest request;

    public CreateUserNegativeTest(CreateUserRequest request, String descriptions) {
        this.request = request;
        this.descriptions = descriptions;
    }

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @Parameterized.Parameters(name = "{1}")
    public static Object[][] testData() {

        return new Object[][] {
                {GeneratorUser.getUserWithoutEmail(), "without email"},
                {GeneratorUser.getUserWithoutPassword(), "without password"},
                {GeneratorUser.getUserWithoutName(), "without name"}
        };
    }

    @Test
    @DisplayName("Create user without data")
    @Description("Return 403 Forbidden and valid response")
    public void createUserReturn403FieldIsNull() {
        Response response = userSteps.createUser(request);
        response.then().statusCode(SC_FORBIDDEN);

        CreateUserForbiddenResponse createUserForbiddenResponse = response.as(CreateUserForbiddenResponse.class);

        assertFalse(MESSAGE_SUCCESS_IS_NOT_FALSE, createUserForbiddenResponse.isSuccess());
        assertEquals(MESSAGE_FIELD_MESSAGE_NOT_MATCH, CREATE_USER_FIELD_MESSAGE_NULL_FIELD, createUserForbiddenResponse.getMessage());

        if (response.statusCode() == SC_OK) {
            accessToken = response.path("accessToken");
        }
    }


    @After
    @DisplayName("Delete user if request was successful and user was created")
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }

}

