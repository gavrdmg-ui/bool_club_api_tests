package models.localstorage;

public record AuthModel(User user,
                        String accessToken,
                        String refreshToken,
                        boolean isAuthenticated) {
}
