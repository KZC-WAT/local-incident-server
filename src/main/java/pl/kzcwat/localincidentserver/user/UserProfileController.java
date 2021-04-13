package pl.kzcwat.localincidentserver.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kzcwat.localincidentserver.user.dto.ChangePasswordDto;
import pl.kzcwat.localincidentserver.user.dto.GetUserResponseDto;
import pl.kzcwat.localincidentserver.user.exception.InvalidCredentialsException;
import pl.kzcwat.localincidentserver.user.exception.UserNotFoundException;
import pl.kzcwat.localincidentserver.user.request.LoginRequest;
import pl.kzcwat.localincidentserver.user.request.RegisterRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class UserProfileController {

    private final UserProfileService userService;

    @GetMapping("{username}")
    public ResponseEntity<GetUserResponseDto> getUserByUsername(@PathVariable String username) {
        try {
            GetUserResponseDto user = userService.getUserByUsername(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<GetUserResponseDto> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            GetUserResponseDto user = userService.register(registerRequest);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{username}")
                    .buildAndExpand(user.getUsername())
                    .toUri();
            return ResponseEntity.created(location).body(user);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String jwt = userService.login(loginRequest);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //Could not test this endpoint right now. Should implement jwt first.
    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            String response = userService.changePassword(changePasswordDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>("Invalid credentials.", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable UUID id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
