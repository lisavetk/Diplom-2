package ru.yandex.praktikum.order.create.test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.order.create.OrderSteps;
import ru.yandex.praktikum.order.create.data.GeneratorOrder;
import ru.yandex.praktikum.order.create.request.CreateOrderRequest;
import ru.yandex.praktikum.order.create.response.CreateOrderSuccessResponse;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.yandex.praktikum.config.TestsMessage.*;
import static ru.yandex.praktikum.config.TestsMessage.MESSAGE_SUCCESS_IS_NOT_TRUE;

@DisplayName("Create order without auth")
public class CreateOrderWithoutAuthTest {
    OrderSteps orderSteps = new OrderSteps();

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    //выделила в отдельный класс негативный тест без авторизации,
    //т.к. по документации "Только авторизованные пользователи могут делать заказы"
    @Test
    @DisplayName("Create order without auth, data is valid")
    @Description("Return 200 OK and valid response")
    public void createOrderReturn401WithoutAuthDataIsValid() {
        CreateOrderRequest request = GeneratorOrder.getOrder();
        Response response = orderSteps.createOrder(request);
        response.then().statusCode(SC_UNAUTHORIZED);

        CreateOrderSuccessResponse createOrderSuccessResponse = response.as(CreateOrderSuccessResponse.class);
        assertNotNull(MESSAGE_ORDER_NAME_IS_NULL, createOrderSuccessResponse.getName());
        assertNotNull(MESSAGE_ORDER_NUMBER_IS_NULL, createOrderSuccessResponse.getOrder().getNumber());
        assertNotNull(MESSAGE_ORDER_IS_NULL, createOrderSuccessResponse.getOrder());
        assertTrue(MESSAGE_SUCCESS_IS_NOT_TRUE, createOrderSuccessResponse.isSuccess());
    }

}
