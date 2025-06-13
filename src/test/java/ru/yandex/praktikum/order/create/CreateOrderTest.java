package ru.yandex.praktikum.order.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.user.GeneratorUser;
import ru.yandex.praktikum.user.UserSteps;
import ru.yandex.praktikum.user.create.CreateUserRequest;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static ru.yandex.praktikum.TestsConstants.CREATE_ORDER_FIELD_MESSAGE_NULL_INGREDIENTS;
import static ru.yandex.praktikum.TestsMessage.*;

@DisplayName("Create order")
public class CreateOrderTest {
    OrderSteps orderSteps = new OrderSteps();
    String accessToken;
    UserSteps userSteps = new UserSteps();

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @Test
    @DisplayName("Create order without auth, data is valid")
    @Description("Return 200 OK and valid response")
    public void createOrderReturn200WithoutAuthDataIsValid() {
        CreateOrderRequest request = GeneratorOrder.getOrder();
        Response response = orderSteps.createOrder(request);
        response.then().statusCode(SC_OK);

        CreateOrderSuccessResponse createOrderSuccessResponse = response.as(CreateOrderSuccessResponse.class);
        assertNotNull(MESSAGE_ORDER_NAME_IS_NULL, createOrderSuccessResponse.getName());
        assertNotNull(MESSAGE_ORDER_NUMBER_IS_NULL, createOrderSuccessResponse.getOrder().getNumber());
        assertNotNull(MESSAGE_ORDER_IS_NULL, createOrderSuccessResponse.getOrder());
        assertTrue(MESSAGE_SUCCESS_IS_NOT_TRUE, createOrderSuccessResponse.isSuccess());
    }

    @Test
    @DisplayName("Create order with auth, data is valid")
    @Description("Return 200 OK and valid response")
    public void createOrderReturn200WithAuthDataIsValid() {
        CreateUserRequest createUserRequest = GeneratorUser.getRandomUser();
        accessToken = userSteps.createUser(createUserRequest).path("accessToken");

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
        Response response = orderSteps.createOrder(request);
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
        Response response = orderSteps.createOrder(request);
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
