package pl.kzcwat.localincidentserver.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
