package pl.kzcwat.localincidentserver.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.security.ApplicationUserRole;
import pl.kzcwat.localincidentserver.user.dto.GetUserResponseDto;
import pl.kzcwat.localincidentserver.user.request.RegisterRequest;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserProfileFactory {

    private final PasswordEncoder passwordEncoder;

    public GetUserResponseDto entityToGetUserResponseDto(UserProfile userProfile) {
        return GetUserResponseDto.builder()
                .id(userProfile.getId())
                .username(userProfile.getUsername())
                .creationDate(userProfile.getCreationDate())
                .upvotes(userProfile.getUpvotes())
                .build();
    }

    public UserProfile registerRequestToEntity(RegisterRequest registerRequest) {
        return UserProfile.builder()
                .creationDate(LocalDateTime.now())
                .email(registerRequest.getEmail())
                .enabled(true)
                .expired(false)
                .locked(false)
                .credentialsExpired(false)
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .upvotes(0L)
                .username(registerRequest.getUsername())
                .role(ApplicationUserRole.USER)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();
    }
}
