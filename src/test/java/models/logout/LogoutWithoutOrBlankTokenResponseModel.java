package models.logout;

import java.util.List;

public record LogoutWithoutOrBlankTokenResponseModel(List<String> refresh) {
}
