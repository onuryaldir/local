package org.development.authms.service;

import org.development.authms.entity.AuthRequest;
import org.development.authms.entity.User;
import org.development.authms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(User user) {

        try {
            if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("This email already exists.");
        }
            user.setReferenceKey(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User user1 = userRepository
                    .save(user);
            user1.setMessage("Success");
            return user;
        } catch (Exception e) {
            user.setMessage(e.getMessage());
            return user;
        }
    }

    @Transactional
    public String deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeleted(true);
            userRepository.save(user);
            return "Success";
        }
        return "User not found";
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
    public boolean authenticate(AuthRequest authRequest) {
        try {
            Optional<User> userOptional = userRepository
                    .findByEmail(authRequest.getEmail());
            return userOptional.filter(user -> verifyPassword(authRequest.getPassword(), user.getPassword())).isPresent();
        } catch (Exception e) {
            return false;
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

            Optional<User> existingUserOptional = userRepository.findByReferenceKey(user.getReferenceKey());
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();

                user.setId(existingUser.getId());
                user.setEmail(existingUser.getEmail());

                existingUser.setName(user.getName());
                existingUser.setSurname(user.getSurname());
                existingUser.setPassword(user.getPassword());
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                existingUser.setBirthDate(user.getBirthDate());
                existingUser.setPhoneNumber(user.getPhoneNumber());

                return userRepository.save(existingUser);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Optional<User> findUserByReferenceKey(String referenceKey) {
        return userRepository.findByReferenceKey(referenceKey);
    }

    public User findUserByEmail(String email) {
        try {
            Optional<User> userOptional = userRepository
                    .findByEmail(email);
            return userOptional.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
