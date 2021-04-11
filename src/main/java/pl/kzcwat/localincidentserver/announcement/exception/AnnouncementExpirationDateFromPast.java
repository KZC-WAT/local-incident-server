package pl.kzcwat.localincidentserver.announcement.exception;

public class AnnouncementExpirationDateFromPast extends RuntimeException {
    public AnnouncementExpirationDateFromPast() {
        super("Provided announcement expiration date cannot be in the past");
    }
}
