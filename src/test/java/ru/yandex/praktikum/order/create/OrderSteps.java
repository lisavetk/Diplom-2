package ru.yandex.praktikum.order.create;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.Endpoints.*;

public class OrderSteps {

    @Step("Send POST request to /api/orders with authorization")
    public Response createOrder(CreateOrderRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .post(API_CREATE_ORDER);
    }

    @Step("Send POST request to /api/orders without authorization")
    public Response createOrder(CreateOrderRequest request, String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .body(request)
                .when()
                .post(API_CREATE_ORDER);
    }

    @Step("Send GET to /api/ingredients")
    public Response getIngredients() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get(API_GET_INGREDIENTS);
    }
}
