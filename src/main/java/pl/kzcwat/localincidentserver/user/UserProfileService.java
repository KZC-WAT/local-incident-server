package pl.kzcwat.localincidentserver.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.user.dto.ChangePasswordDto;
import pl.kzcwat.localincidentserver.user.dto.GetUserResponseDto;
import pl.kzcwat.localincidentserver.user.exception.InvalidCredentialsException;
import pl.kzcwat.localincidentserver.user.exception.UserNotFoundException;
import pl.kzcwat.localincidentserver.user.request.LoginRequest;
import pl.kzcwat.localincidentserver.user.request.RegisterRequest;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserProfileService implements UserDetailsService {

    private final UserProfileRepository userRepository;
    private final UserProfileFactory userFactory;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));
    }

    public String login(LoginRequest loginRequest) throws UserNotFoundException, InvalidCredentialsException {
        UserProfile user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(InvalidCredentialsException::new);
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            //If implementing jwt please add jwt token generating.
            return "Successfully logged in.";
        }
        throw new InvalidCredentialsException();
    }

    public GetUserResponseDto register(RegisterRequest registerRequest) throws IllegalArgumentException {
        if (userRepository.existsByUsername(registerRequest.getUsername()) || userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException();
        }
        UserProfile registeredUser = userRepository.save(userFactory.registerRequestToEntity(registerRequest));
        return userFactory.entityToGetUserResponseDto(registeredUser);
    }

    public GetUserResponseDto getUserByUsername(String username) throws UserNotFoundException {
        UserProfile userProfile = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        return userFactory.entityToGetUserResponseDto(userProfile);
    }

    public void deleteUserById(UUID id) {
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(id);
    }

    //Could not implement this function right now. Should implement jwt first and check subject.
    public String changePassword(ChangePasswordDto changePasswordDto) {
        //get user by username from subject
        //validate user password with changePasswordDto.getCurrentPassword
        //if true then change password
        //else throw new InvalidCredentialException
        return null;
    }
}
