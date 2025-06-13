package ru.yandex.praktikum;

public class TestsMessage {
    //валидные данные
    public static final String MESSAGE_SUCCESS_IS_NOT_TRUE = "Поле success не true";
    public static final String MESSAGE_EMAIL_NOT_MATCH = "Email не совпадает с использованным для регистрации";
    public static final String MESSAGE_USER_NAME_NOT_MATCH = "Name не совпадает с использованным для регистрации";
    public static final String MESSAGE_ACCESS_TOKEN_IS_NULL = "accessToken пустой";
    public static final String MESSAGE_REFRESH_TOKEN_IS_NULL = "refreshToken пустой";

    public static final String MESSAGE_ORDER_NAME_IS_NULL = "Поле name пустое";
    public static final String MESSAGE_ORDER_NUMBER_IS_NULL = "Поле order.number пустое";
    public static final String MESSAGE_ORDER_IS_NULL = "Заказ пустой";

    //невалидные данные
    public static final String MESSAGE_SUCCESS_IS_NOT_FALSE = "Поле success не false";
    public static final String MESSAGE_FIELD_MESSAGE_NOT_MATCH = "Поле message не совпадает с документацией";

}
