package ru.yandex.praktikum.user.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserUnauthorizedResponse {
    private boolean success;
    private String message;
}
