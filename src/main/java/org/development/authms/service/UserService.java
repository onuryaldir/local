package org.development.authms.service;

import jakarta.ws.rs.NotFoundException;
import org.development.authms.entity.*;
import org.development.authms.repository.UserRepository;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private JwtService jwtService;

    private final ModelMapper modelMapper = new ModelMapper();

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(RegisterRequest request) {

        User user = convertToUser(request);

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
    public AuthResponse authenticate(AuthRequest authRequest) {
        AuthResponse response = new AuthResponse();

        try {

            Optional<User> userOptional = userRepository
                    .findByEmail(authRequest.getEmail());
           userOptional.filter(user -> verifyPassword(authRequest.getPassword(),user.getPassword())).ifPresentOrElse(user ->{
               response.setEmail(user.getEmail());
               response.setId(user.getId());
               response.setPhoneNumber(user.getPhoneNumber());
               response.setName(user.getName());
               response.setDeleted(user.isDeleted());
               response.setSurname(user.getSurname());
               response.setBirthDate(user.getBirthDate());
               response.setAccessToken(jwtService.generate(user.getEmail(),"ACCESS"));
               response.setRefreshToken(jwtService.generate(user.getEmail(),"REFRESH"));
               response.setReferenceKey(user.getReferenceKey());
               response.setMessage("Success");
           }, () -> {

               response.setMessage("Invalid Credentials");
           } );

           return response;
        } catch (Exception e){

            response.setMessage(e.getMessage());
           return response;
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

    public User updateUser(Long userId, UpdateUserRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (updateRequest.getName() != null) {
            user.setName(updateRequest.getName());
        }
        if (updateRequest.getSurname() != null) {
            user.setSurname(updateRequest.getSurname());
        }
        if (updateRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        if (updateRequest.getBirthDate() != null) {
            user.setBirthDate(updateRequest.getBirthDate());
        }
        if (updateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(updateRequest.getPhoneNumber());
        }

        return userRepository.save(user);
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

    public AuthResponse refresh(String token) {

        String email =jwtService.getAllClaimsFromToken(token).get("email").toString();
        AuthResponse response = new AuthResponse();

        Optional<User> userOptional = userRepository
                .findByEmail(email);
        userOptional.ifPresentOrElse(user ->{
            response.setEmail(user.getEmail());
            response.setId(user.getId());
            response.setPhoneNumber(user.getPhoneNumber());
            response.setName(user.getName());
            response.setDeleted(user.isDeleted());
            response.setSurname(user.getSurname());
            response.setBirthDate(user.getBirthDate());
            response.setAccessToken(jwtService.generate(user.getEmail(),"ACCESS"));
            response.setRefreshToken(jwtService.generate(user.getEmail(),"REFRESH"));
            response.setReferenceKey(user.getReferenceKey());
            response.setMessage("Success");
        }, ()-> {response.setMessage("Invalid Credentials");} );

        return response;

    }


    public User convertToUser(RegisterRequest registerRequest) {
        User user = modelMapper.map(registerRequest, User.class);
        user.setReferenceKey(UUID.randomUUID().toString());
        user.setDeleted(false);
        return user;
    }
}
