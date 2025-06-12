package ru.yandex.praktikum.user.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserSuccessResponse {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String email;
        private String name;
    }
}
