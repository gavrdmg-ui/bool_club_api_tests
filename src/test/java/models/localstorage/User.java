package models.localstorage;

public record User(Integer id,
                   String username,
                   String firstName,
                   String lastName,
                   String email,
                   String remoteAddr) {
}
