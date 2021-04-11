package pl.kzcwat.localincidentserver.userprofile.exception;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException() {
        super("User profile with provided id was not found");
    }
}
