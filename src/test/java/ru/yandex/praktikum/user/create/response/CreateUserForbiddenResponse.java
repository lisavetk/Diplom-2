package ru.yandex.praktikum.user.create.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserForbiddenResponse {
    private boolean success;
    private String message;
}
