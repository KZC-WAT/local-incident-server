package pl.kzcwat.localincidentserver.announcementcategory;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
public class AnnouncementCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private AnnouncementCategory superCategory;

    @Size(min = 3)
    private String name;
}
