package org.development.authms.service;

import org.development.authms.entity.User;
import org.development.authms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        try {
            user.setReferenceKey(UUID.randomUUID().toString());
            User user1 = userRepository
                    .save(user);
            user1.setMessage("Success");
            return user;
        } catch (Exception e) {
            user.setMessage(e.getMessage());
            return user;
        }
    }

    public String deleteUser(Long id) {
        try {
             userRepository
                    .deleteById(id);
            return "Success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public User findUserById(Long id) {
        try {
            Optional<User> userOptional = userRepository
                    .findById(id);
            return userOptional.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public User findUserByReferenceKey(User user) {
        try {
            Optional<User> userOptional = userRepository
                    .findByReferenceKey(user.getReferenceKey());
            return userOptional.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public User updateUser(User user) {
        try {
            User existingUser = findUserByReferenceKey(user);
            if (Objects.nonNull(existingUser)) {
                user.setId(existingUser.getId());
                user.setEmail(existingUser.getEmail());
                return userRepository.save(user);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
