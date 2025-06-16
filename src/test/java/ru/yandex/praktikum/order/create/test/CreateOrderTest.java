package ru.yandex.praktikum.order.create.test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.order.create.data.GeneratorOrder;
import ru.yandex.praktikum.order.create.OrderSteps;
import ru.yandex.praktikum.order.create.request.CreateOrderRequest;
import ru.yandex.praktikum.order.create.response.CreateOrderBadRequestResponse;
import ru.yandex.praktikum.order.create.response.CreateOrderSuccessResponse;
import ru.yandex.praktikum.user.data.GeneratorUser;
import ru.yandex.praktikum.user.UserSteps;
import ru.yandex.praktikum.user.create.request.CreateUserRequest;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static ru.yandex.praktikum.config.TestsConstants.CREATE_ORDER_FIELD_MESSAGE_NULL_INGREDIENTS;
import static ru.yandex.praktikum.config.TestsMessage.*;

@DisplayName("Create order with auth")
public class CreateOrderTest {
    OrderSteps orderSteps = new OrderSteps();
    String accessToken;
    UserSteps userSteps = new UserSteps();

    @Before
    @Step("Create user")
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
        CreateUserRequest createUserRequest = GeneratorUser.getRandomUser();
        accessToken = userSteps.createUser(createUserRequest).path("accessToken");
    }

    //выделила в отдельный класс тесты, которые с авторизацией, т.к. создание пользователя перенесено в @before
    //сделано на основе документации "Только авторизованные пользователи могут делать заказы"

    @Test
    @DisplayName("Create order with auth, data is valid")
    @Description("Return 200 OK and valid response")
    public void createOrderReturn200WithAuthDataIsValid() {
        CreateOrderRequest request = GeneratorOrder.getOrder();
        Response response = orderSteps.createOrder(request, accessToken);
        response.then().statusCode(SC_OK);

        CreateOrderSuccessResponse createOrderSuccessResponse = response.as(CreateOrderSuccessResponse.class);
        assertNotNull(MESSAGE_ORDER_NAME_IS_NULL, createOrderSuccessResponse.getName());
        assertNotNull(MESSAGE_ORDER_NUMBER_IS_NULL, createOrderSuccessResponse.getOrder().getNumber());
        assertNotNull(MESSAGE_ORDER_IS_NULL, createOrderSuccessResponse.getOrder());
        assertTrue(MESSAGE_SUCCESS_IS_NOT_TRUE, createOrderSuccessResponse.isSuccess());
    }

    @Test
    @DisplayName("Create order with null ingredients")
    @Description("Return 400 Bad Request and valid response")
    public void createOrderReturn400WithNullIngredients() {
        CreateOrderRequest request = GeneratorOrder.getOrderWithoutIngredients();
        Response response = orderSteps.createOrder(request, accessToken);
        response.then().statusCode(SC_BAD_REQUEST);

        CreateOrderBadRequestResponse createOrderBadRequestResponse = response.as(CreateOrderBadRequestResponse.class);
        assertFalse(MESSAGE_SUCCESS_IS_NOT_FALSE, createOrderBadRequestResponse.isSuccess());
        assertEquals(MESSAGE_FIELD_MESSAGE_NOT_MATCH, CREATE_ORDER_FIELD_MESSAGE_NULL_INGREDIENTS, createOrderBadRequestResponse.getMessage());

    }

    @Test
    @DisplayName("Create order with invalid hash ingredients")
    @Description("Return 500 Internal Server Error")
    public void createOrderReturn500WithInvalidHashIngredients() {
        CreateOrderRequest request = GeneratorOrder.getOrderWithInvalidHashIngredient();
        Response response = orderSteps.createOrder(request, accessToken);
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    @DisplayName("Delete created user")
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
