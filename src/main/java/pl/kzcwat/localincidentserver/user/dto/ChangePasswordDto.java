package pl.kzcwat.localincidentserver.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
public class ChangePasswordDto {

    @Size(min = 8)
    private String currentPassword;

    @Size(min = 8)
    private String newPassword;

    @Size(min = 8)
    private String newPasswordConfirmation;
}
