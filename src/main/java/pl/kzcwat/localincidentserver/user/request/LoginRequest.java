package pl.kzcwat.localincidentserver.user.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class LoginRequest {

    @NotNull
    @Size(min = 4)
    private String username;
    @NotNull
    @Size(min = 8)
    private String password;
}
