package models.registration;

import java.util.List;

public record RegistrationWithoutOrBlankFieldsResponseModel(List<String> username, List<String> password) {
}
