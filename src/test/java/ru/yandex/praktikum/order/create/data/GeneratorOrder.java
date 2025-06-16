package ru.yandex.praktikum.order.create.data;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.order.create.OrderSteps;
import ru.yandex.praktikum.order.create.request.CreateOrderRequest;

import java.util.ArrayList;
import java.util.List;

public class GeneratorOrder {

    @Step("Create order's valid data")
    public static CreateOrderRequest getOrder() {
        OrderSteps orderSteps = new OrderSteps();
        Response responseGetIngredients = orderSteps.getIngredients();
        List<String> ingredients = responseGetIngredients.path("data._id");
        List<String> orderIngredients = new ArrayList<>();
        orderIngredients.add(ingredients.get(0));
        orderIngredients.add(ingredients.get(1));
        orderIngredients.add(ingredients.get(4));
        return new CreateOrderRequest(orderIngredients);
    }

    @Step("Create order's data: list of ingredients is null")
    public static CreateOrderRequest getOrderWithoutIngredients() {
        List<String> orderIngredients = new ArrayList<>();
        return new CreateOrderRequest(orderIngredients);
    }

    @Step("Create order' data: ingredient's hash is invalid")
    public static CreateOrderRequest getOrderWithInvalidHashIngredient() {
        List<String> orderIngredients = new ArrayList<>();
        orderIngredients.add("InvalidHash");
        return new CreateOrderRequest(orderIngredients);
    }
}
