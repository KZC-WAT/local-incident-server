package pl.kzcwat.localincidentserver.announcement;

import lombok.Data;
import pl.kzcwat.localincidentserver.region.Region;
import pl.kzcwat.localincidentserver.userprofile.UserProfile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "announcement")
@Data
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "publication_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime publicationDate;

    @Column(name = "expiration_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime expitationDate;

    @ManyToOne
    private Region region;

    @ManyToOne
    private UserProfile author;
}
