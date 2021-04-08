package pl.kzcwat.localincidentserver.announcement;

import lombok.*;
import pl.kzcwat.localincidentserver.region.Region;
import pl.kzcwat.localincidentserver.userprofile.UserProfile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "announcement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "publication_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    @Builder.Default
    private LocalDateTime publicationDate = LocalDateTime.now();

    @Column(name = "expiration_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime expitationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Region region;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private UserProfile author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
}
