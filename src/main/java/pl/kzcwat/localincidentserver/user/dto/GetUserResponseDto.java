package pl.kzcwat.localincidentserver.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class GetUserResponseDto {

    private UUID id;
    private String username;
    private Long upvotes;
    private LocalDateTime creationDate;
}
