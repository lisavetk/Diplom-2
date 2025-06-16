package ru.yandex.praktikum.user.login.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserSuccessResponse {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private User user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String email;
        private String name;
    }
}
