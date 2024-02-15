package org.development.authms.repository;

import lombok.NoArgsConstructor;
import org.development.authms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByReferenceKey(String referenceKey);
}
