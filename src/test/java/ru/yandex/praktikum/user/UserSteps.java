package ru.yandex.praktikum.user;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.user.create.CreateUserRequest;
import ru.yandex.praktikum.user.login.LoginUserRequest;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.Endpoints.*;

public class UserSteps {

    @Step("Send POST request to /api/auth/register")
    public Response createUser(CreateUserRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .post(API_CREATE_USER);
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response deleteUser(String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .delete(API_DELETE_USER);
    }

    @Step("Send POST request to /api/auth/login")
    public Response loginUser(LoginUserRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .post(API_LOGIN_USER);
    }


}
