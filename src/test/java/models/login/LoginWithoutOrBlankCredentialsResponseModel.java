package models.login;

import java.util.List;

public record LoginWithoutOrBlankCredentialsResponseModel(List<String> username, List<String> password) {
}
