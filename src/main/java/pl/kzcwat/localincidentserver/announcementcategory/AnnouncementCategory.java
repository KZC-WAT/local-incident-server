package pl.kzcwat.localincidentserver.announcementcategory;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Data
public class AnnouncementCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private AnnouncementCategory superCategory;

    @Size(min = 3)
    private String name;
}
