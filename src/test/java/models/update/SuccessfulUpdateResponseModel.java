package models.update;

public record SuccessfulUpdateResponseModel(int id, String username, String firstName, String lastName, String email,
                                            String remoteAddr) {
}
